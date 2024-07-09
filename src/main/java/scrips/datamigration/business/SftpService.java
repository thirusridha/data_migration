package scrips.datamigration.business;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

@Service
public class SftpService {
	

		private Logger logger = LoggerFactory.getLogger(SftpService.class);
		
		 @Autowired
		    private Environment env;

	
		public List<String> readFileFromSFTPDirectroy(String remoteFilePath, String remoteFileName) {
			boolean status = false;
			List<String> fileRecords = new ArrayList<String>();
			ChannelSftp channelSftp = createChannelSftp();
			String fileNameWithPath = remoteFilePath + remoteFileName;
			try (InputStream fileStream = channelSftp.get(fileNameWithPath);
					Scanner sc = new Scanner(fileStream).useDelimiter("\\A")) {
				while (sc.hasNextLine()) {
					fileRecords.add(sc.nextLine());
				}
				status = true;
				return fileRecords;
			} catch (SftpException ex) {
				logger.error("Error download file", ex);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (status) {
						checkAndCreateDir(remoteFilePath + "OK", channelSftp);
						moveFileToSpecificDir(channelSftp, remoteFilePath, "/OK/", addDateTimeToFileName(remoteFileName), remoteFileName);
					} else {
						checkAndCreateDir(remoteFilePath + "ERROR", channelSftp);
						moveFileToSpecificDir(channelSftp, remoteFilePath, "/ERROR/", addDateTimeToFileName(remoteFileName), remoteFileName);
					}
				} catch (SftpException e) {
					e.printStackTrace();
				}
				disconnectChannelSftp(channelSftp);

			}
			return fileRecords;
		}
		
		private void moveFileToSpecificDir(ChannelSftp channelSftp,String FileDirectory,String newDir,String newFile,String existingfile) throws SftpException
		{
			channelSftp.cd(FileDirectory);
			if (channelSftp.get( existingfile ) != null){
				channelSftp.rename(FileDirectory + existingfile , 
			        FileDirectory + newDir + newFile );
			}
		}
		
		private String addDateTimeToFileName(String fileName)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
			String date = sdf.format(new Date());
			return fileName.substring(0,fileName.lastIndexOf("."))+"_"+date+".txt";
		}
		private void checkAndCreateDir(String dir, ChannelSftp channelSftp) {
			try {
				String currentDirectory = channelSftp.pwd();

				SftpATTRS attrs = null;
				try {
					attrs = channelSftp.stat(currentDirectory + "/" + dir);
				} catch (Exception e) {
					System.out.println(currentDirectory + "/" + dir + " not found");
				}

				if (attrs != null) {
					System.out.println("Directory exists IsDir=" + attrs.isDir());
				} else {
					System.out.println("Creating dir " + dir);
					channelSftp.mkdir(dir);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private ChannelSftp createChannelSftp() {
			try {
				JSch jSch = new JSch();
				//Session session = jSch.getSession(username, host, port);
				Session session = jSch.getSession(env.getProperty("FTP.USER_NAME"), env.getProperty("FTP.IP_ADDRESS"), 
						Integer.parseInt(env.getProperty("FTP.PORT")));
				session.setPassword(env.getProperty("FTP.PASSWORD"));
				session.setConfig("StrictHostKeyChecking", "no");
				//session.setPassword(password);
				session.connect(10000);
				Channel channel = session.openChannel("sftp");
				channel.connect(10000);
				return (ChannelSftp) channel;
			} catch(JSchException ex) {
				logger.error("Create ChannelSftp error", ex);
			}
			
			return null;
		}
		
		private void disconnectChannelSftp(ChannelSftp channelSftp) {
			try {
				if( channelSftp == null) 
					return;
				
				if(channelSftp.isConnected()) 
					channelSftp.disconnect();
				
				if(channelSftp.getSession() != null) 
					channelSftp.getSession().disconnect();
				
			} catch(Exception ex) {
				logger.error("SFTP disconnect error", ex);
			}
		}
		
	
}
