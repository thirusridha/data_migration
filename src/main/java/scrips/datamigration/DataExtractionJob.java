package scrips.datamigration;

import java.sql.*;

//import bcsis.meps.rtgs.online.common.*;
//import bcsis.meps.rtgs.online.business.*;

/**
* Program amendment history
* @ver		20-Mar-2006
* @auth		Asokan Srinivasan
* @change	CC-032
*			printStackTrace() statements removed from the following method
*				 main(String[] args).				
* 
* @ver    15-Jul-2008
* @auth   Asokan
* @change MEPSSUP000000044
*         15 digits of RTGS Txn Reference used in data extraction 
*
* @version 03-July-2015
* @author  Eswari
* @change  MEPSSUP-2014-0092_2
* 			1. Testing codes mixed with production codes
*/


public class DataExtractionJob
{
//    private static final String debugString = "DataExtractionJob";
//
//    private Query qry = null;
//    private java.sql.Connection conn = null;
//    private String Archive_Dir ="";
//    
//    public DataExtractionJob() throws Exception
//    {
//        qry = new Query();
//        conn = qry.getCurrConnection();
//        Config curconfig=new Config();
//        Archive_Dir=curconfig.getArchiveDir();
//
//    }
//    
//
//    public void process() throws Exception
//    {
//        try
//        {
//            extractInMsgLog();
//            extractTxnDetail();
//            extractAccPosition();
//            extractMbrPosition();
////            TECLogging.log(7,"Job","BTH0013","EOD#Daily Extraction for Data Archiving");
//        }
//        catch (Exception ex)
//        {
////            TECLogging.log(9,"Job","BTH0014","EOD#Daily Extraction for Data Archiving");
//            TECLogging.log(9,"Job","BTH0002",ex.toString());          
//            RtgsException.log(1, debugString, "process():L39", ex);
//            throw new Exception(ex.getMessage());
//        }
//        finally
//        {
//            if (qry != null) {
//                qry.close();
//                qry=null;
//            }
//        }
//    }
//
//    /**
//    * Extract all record in MBR_POSITION
//    *
//    * @return
//    */
//    public void extractMbrPosition() throws Exception
//    {
//        PreparedStatement pstmt1 = null;
//        ResultSet rset1 = null;
//        String sqlstmt1 = null;
//        boolean contflag = true;
//        String startseq = "20050101";
//        long reclmt = 100;
//        long totalnum = 0;
//        long loopnum = 0;
//        String seqno="";
//        String recstr = "";
//        String msgstr = "";
//        StringBuffer outstr = null;
//        Clob clob = null;
//        General curgeneral = new General();
//        String dtstamp = curgeneral.getCurDateTime("yyyyMMddHHmmss");
//
//        try
//        {
///*            sqlstmt1 = "SELECT ML_SEQ_NO, ML_DATA, ML_MSG_IMAGE FROM " +
//                       "(SELECT ML_SEQ_NO, " +
//                       "ML_SEQ_NO ||'|'|| ML_RTGS_REF_NO ||'|'|| " +
//                       "ML_MSG_SENDER_CODE ||'|'|| ML_MSG_RECEIVER_CODE ||'|'|| ML_MSG_TYPE ||'|'|| " +
//                       "ML_TXN_DATE ||'|'|| ML_VAL_DATE ||'|'|| ML_FIELD_20 ||'|'|| " +
//                       "ML_TXN_AMT ||'|'|| " +
//                       "TO_CHAR(ML_RECEIVED_STAMP, 'YYYYMMDDHH24MISS') AS ML_DATA, " +
//                       "ML_MSG_IMAGE " +
//                       "FROM MSG_LOG WHERE ML_SEQ_NO > ? ORDER BY ML_SEQ_NO) " +
//                       "WHERE ROWNUM <= ? ";
//*/
//            sqlstmt1 = "SELECT MP_KEY, MP_DATA FROM " +
//                       "(SELECT MP_VAL_DATE || MP_ACC_NO || MP_MBR_CODE AS MP_KEY, " +
//                       "MP_VAL_DATE ||'|'|| MP_ACC_NO ||'|'|| " +
//                       "MP_MBR_CODE ||'|'|| MP_CCY_CODE ||'|'|| MP_SGS_TXN_CR_CNT ||'|'|| " +
//                       "MP_SGS_TXN_DR_CNT ||'|'|| MP_SGS_TXN_CR_AMT ||'|'|| MP_SGS_TXN_DR_AMT ||'|'|| " +
//                       "MP_CLEARING_TXN_CR_CNT ||'|'|| MP_CLEARING_TXN_DR_CNT ||'|'|| MP_CLEARING_TXN_CR_AMT ||'|'|| MP_CLEARING_TXN_DR_AMT ||'|'|| " +
//                       "MP_CDP_TXN_CR_CNT ||'|'|| MP_CDP_TXN_DR_CNT ||'|'|| MP_CDP_TXN_CR_AMT ||'|'|| MP_CDP_TXN_DR_AMT ||'|'|| " +
//                       "MP_OTHER_TXN_CR_CNT ||'|'|| MP_OTHER_TXN_DR_CNT ||'|'|| MP_OTHER_TXN_CR_AMT ||'|'|| MP_OTHER_TXN_DR_AMT ||'|'|| " +
//                       "MP_TR_FR_RESERVED_CR ||'|'|| MP_TR_FR_RESERVED_CR_CNT ||'|'|| MP_TR_FR_RESERVED_DR ||'|'|| MP_TR_FR_RESERVED_DR_CNT ||'|'|| " +
//                       "MP_TR_TO_RESERVED_CR ||'|'|| MP_TR_TO_RESERVED_CR_CNT ||'|'|| MP_TR_TO_RESERVED_DR ||'|'|| MP_TR_TO_RESERVED_DR_CNT ||'|'|| " +
//                       "MP_OPENING_BAL ||'|'|| MP_CLOSING_BAL ||'|'|| MP_COLLATERAL_AMT ||'|'|| MP_CANCEL_TXN_CNT ||'|'||  MP_CANCEL_TXN_AMT AS MP_DATA " +
//                       "FROM MBR_POSITION WHERE (MP_VAL_DATE || MP_ACC_NO || MP_MBR_CODE) > ? order by MP_VAL_date ASC,MP_ACC_NO ASC,MP_mbr_code ASC) " +
//                       "WHERE ROWNUM <= ? ";
//
//            pstmt1 = conn.prepareStatement(sqlstmt1);
//
//            while(contflag)
//            {
//                RtgsException.log(0, debugString, "Checking for records ... Current count:"+totalnum);
//                pstmt1.clearParameters();
//                pstmt1.setString(1,startseq);
//                pstmt1.setLong(2,reclmt);
//                rset1 = pstmt1.executeQuery();
//                loopnum = 0;
//                outstr = new StringBuffer();
//
//                //System.out.println("@@startseq>>"+startseq);
//                //System.out.println("@@reclmt>>"+reclmt);
//                //System.out.println("@@loopnum>>"+loopnum);
//                while(rset1.next())
//                {
//                    totalnum++;
//                    loopnum++;
//                    startseq = rset1.getString(1);
//                    recstr   = rset1.getString(2);
//                    //msgstr     = rset1.getString(3);
//                    //outstr = outstr.append(recstr).append("|").append(msgstr).append("\r\n");
//                    outstr = outstr.append(startseq).append("|").append(recstr).append("\r\n");
//                }
//
//                //System.out.println("%%loopnum>>"+loopnum);
//                if (loopnum == 0)
//                {
//                    contflag = false;
//                }else
//                {
//                 curgeneral.writeToFile(Archive_Dir, dtstamp+"_"+"RTGS_mbrpos.txt", outstr.toString(), true,true);            
//                }  
//                
//            }
//
//            if (totalnum > 0)
//            {
//               RtgsException.log(0, debugString, "Writing archive file for mbrpos. Total records:"+totalnum);
//            }else
//            {
//               RtgsException.log(0, debugString, "No records retrieved for mbrpos.");
//            }
//
//
//        }
//        catch (Exception e)
//        {
//            TECLogging.log(9,"Job","BTH0002",e.toString());
//            RtgsException.log(1, debugString, "extractMbrPosition()", e);
//            throw e;
//        }
//        finally
//        {
//            if (rset1 != null) {
//                rset1.close();
//                rset1 = null;
//            }
//            if (pstmt1 != null) {
//                pstmt1.close();
//                pstmt1 = null;
//            }
//        }
//    }  // extractAccPosition()
//
//
//
//    /**
//    * Extract all record in ACC_POSITION
//    *
//    * @return
//    */
//    public void extractAccPosition() throws Exception
//    {
//        PreparedStatement pstmt1 = null;
//        ResultSet rset1 = null;
//        String sqlstmt1 = null;
//        boolean contflag = true;
//        String startseq = " ";
//        long reclmt = 100;
//        long totalnum = 0;
//        long loopnum = 0;
//        String seqno="";
//        String recstr = "";
//        String msgstr = "";
//        StringBuffer outstr = null;
//        Clob clob = null;
//        General curgeneral = new General();
//        String dtstamp = curgeneral.getCurDateTime("yyyyMMddHHmmss");
//
//        try
//        {
///*            sqlstmt1 = "SELECT ML_SEQ_NO, ML_DATA, ML_MSG_IMAGE FROM " +
//                       "(SELECT ML_SEQ_NO, " +
//                       "ML_SEQ_NO ||'|'|| ML_RTGS_REF_NO ||'|'|| " +
//                       "ML_MSG_SENDER_CODE ||'|'|| ML_MSG_RECEIVER_CODE ||'|'|| ML_MSG_TYPE ||'|'|| " +
//                       "ML_TXN_DATE ||'|'|| ML_VAL_DATE ||'|'|| ML_FIELD_20 ||'|'|| " +
//                       "ML_TXN_AMT ||'|'|| " +
//                       "TO_CHAR(ML_RECEIVED_STAMP, 'YYYYMMDDHH24MISS') AS ML_DATA, " +
//                       "ML_MSG_IMAGE " +
//                       "FROM MSG_LOG WHERE ML_SEQ_NO > ? ORDER BY ML_SEQ_NO) " +
//                       "WHERE ROWNUM <= ? ";
//*/
//            sqlstmt1 = "SELECT AP_KEY, AP_DATA FROM " +
//                       "(SELECT AP_MBR_CODE || AP_VAL_DATE AS AP_KEY, " +
//                       "AP_MBR_CODE ||'|'|| AP_VAL_DATE ||'|'|| " +
//                       "AP_ACC_NO ||'|'|| AP_CCY_CODE ||'|'|| AP_CUR_BAL ||'|'|| " +
//                       "AP_ALLOC_NS ||'|'|| AP_ALLOC_1 ||'|'|| AP_ALLOC_2 ||'|'|| " +
//                       "AP_ALLOC_3 ||'|'|| AP_ALLOC_4 ||'|'|| AP_ALLOC_5 ||'|'|| AP_ALLOC_6 ||'|'|| " +
//                       "AP_ALLOC_7 ||'|'|| AP_ALLOC_8 ||'|'|| AP_ALLOC_9 ||'|'|| AP_SOLVED_BAL AS AP_DATA " +
//                       "FROM ACC_POSITION WHERE (AP_MBR_CODE || AP_VAL_DATE) >? ORDER BY AP_MBR_CODE asc,AP_VAL_DATE asc) " +
//                       "WHERE ROWNUM <= ? ";
//
//            pstmt1 = conn.prepareStatement(sqlstmt1);
//
//            while(contflag)
//            {
//                RtgsException.log(0, debugString, "Checking for records ... Current count:"+totalnum);
//                pstmt1.clearParameters();
//                pstmt1.setString(1,startseq);
//                pstmt1.setLong(2,reclmt);
//                rset1 = pstmt1.executeQuery();
//                loopnum = 0;
//                outstr = new StringBuffer();
//   
//                //System.out.println("@@startseq>>"+startseq);
//                //System.out.println("@@reclmt>>"+reclmt);
//                //System.out.println("@@loopnum>>"+loopnum);
//                while(rset1.next())
//                {
//                    totalnum++;
//                    loopnum++;
//                    startseq = rset1.getString(1);
//                    recstr   = rset1.getString(2);
//                    //msgstr     = rset1.getString(3);
//                    //outstr = outstr.append(recstr).append("|").append(msgstr).append("\r\n");
//                    outstr = outstr.append(startseq).append("|").append(recstr).append("\r\n");
//                }
//
//                //System.out.println("%%loopnum>>"+loopnum);
//                if (loopnum == 0)
//                {
//                    contflag = false;
//                }else
//                {
//                  curgeneral.writeToFile(Archive_Dir, dtstamp+"_"+"RTGS_account.txt", outstr.toString(), true,true);
//                }                
//            }
//
//            if (totalnum > 0)
//            {
//                RtgsException.log(0, debugString, "Writing archive file for account. Total records:"+totalnum);
//            }else
//            {
//                RtgsException.log(0, debugString, "No records retrieved for account.");
//            }
//
//        }
//        catch (Exception e)
//        {
//            TECLogging.log(9,"Job","BTH0002",e.toString());
//            RtgsException.log(1, debugString, "extractAccPosition()", e);
//            throw e;
//        }
//        finally
//        {
//            if (rset1 != null) {
//                rset1.close();
//                rset1 = null;
//            }
//            if (pstmt1 != null) {
//                pstmt1.close();
//                pstmt1 = null;
//            }
//        }
//    }  // extractAccPosition()
//
//
//
//    /**
//    * Extract all record in TXN_DETAIL
//    *
//    * @return
//    */
//    public void extractTxnDetail() throws Exception
//    {
//        PreparedStatement pstmt1 = null;
//        ResultSet rset1 = null;
//        String sqlstmt1 = null;
//        boolean contflag = true;
//        long startseq = 0;
//        long reclmt = 100;
//        long totalnum = 0;
//        long loopnum = 0;
//        String seqno="";
//        String recstr = "";
//        String msgstr = "";
//        StringBuffer outstr = null;
//        Clob clob = null;
//        General curgeneral = new General();
//        String dtstamp = curgeneral.getCurDateTime("yyyyMMddHHmmss");
//        
//        try
//        {
///*            sqlstmt1 = "SELECT ML_SEQ_NO, ML_DATA, ML_MSG_IMAGE FROM " +
//                       "(SELECT ML_SEQ_NO, " +
//                       "ML_SEQ_NO ||'|'|| ML_RTGS_REF_NO ||'|'|| " +
//                       "ML_MSG_SENDER_CODE ||'|'|| ML_MSG_RECEIVER_CODE ||'|'|| ML_MSG_TYPE ||'|'|| " +
//                       "ML_TXN_DATE ||'|'|| ML_VAL_DATE ||'|'|| ML_FIELD_20 ||'|'|| " +
//                       "ML_TXN_AMT ||'|'|| " +
//                       "TO_CHAR(ML_RECEIVED_STAMP, 'YYYYMMDDHH24MISS') AS ML_DATA, " +
//                       "ML_MSG_IMAGE " +
//                       "FROM MSG_LOG WHERE ML_SEQ_NO > ? ORDER BY ML_SEQ_NO) " +
//                       "WHERE ROWNUM <= ? ";
//*/
//            //23-Jul-2008 - Asokan - MEPSSUP000000044 - Start
//            //15 digits of RTGS Txn Reference used in data extraction
//            /*sqlstmt1 = "SELECT TO_NUMBER(SUBSTR(TD_RTGS_REF_NO,12)), TD_DATA FROM "+
//                       "(SELECT TD_RTGS_REF_NO, " +
//                       "TD_RTGS_REF_NO ||'|'|| TD_VAL_DATE ||'|'|| " +
//                       "TD_MSG_SENDER_CODE ||'|'|| TD_SENDER_CODE ||'|'|| TD_RECEIVER_CODE ||'|'|| " +
//                       "TD_SENDER_ACC_TYPE ||'|'|| TD_RECEIVER_ACC_TYPE ||'|'|| TD_CCY_CODE ||'|'|| " +
//                       "TD_SWIFT_REF ||'|'|| TD_SWIFT_MUR ||'|'|| TD_TXN_DATE ||'|'|| TD_TXN_TIME ||'|'|| " +
//                       "TD_TXN_STAT ||'|'|| TD_TXN_TYPE ||'|'|| TD_TXN_AMT ||'|'|| TD_DR_CR ||'|'|| " +
//                       "TD_QUEUE_STAT ||'|'|| TD_SWIFT_MT ||'|'|| TD_RELATED_REF ||'|'|| TD_ORDERING_INST ||'|'|| " +
//                       "TD_BENEFICIARY_INST ||'|'|| TD_CHANNEL ||'|'|| TD_RETURN_CODE ||'|'|| TD_ACC_WITH_INST ||'|'|| " +
//                       "TD_REMITT_INFO ||'|'|| TO_CHAR(TD_UPD_DT_STAMP, 'YYYYMMDDHH24MISS') ||'|'|| TD_MBRPOS_TYPE ||'|'|| TD_SENDER_TO_RECEIVER_INFO AS TD_DATA " +
//                       "FROM TXN_DETAIL WHERE TO_NUMBER(SUBSTR(TD_RTGS_REF_NO,12)) > ? ORDER BY TD_RTGS_REF_NO) " +
//                       "WHERE ROWNUM <= ? ";*/
//            sqlstmt1 = "SELECT TO_NUMBER(SUBSTR(TD_RTGS_REF_NO, 2)), TD_DATA FROM "+
//                       "(SELECT TD_RTGS_REF_NO, " +
//                       "TD_RTGS_REF_NO ||'|'|| TD_VAL_DATE ||'|'|| " +
//                       "TD_MSG_SENDER_CODE ||'|'|| TD_SENDER_CODE ||'|'|| TD_RECEIVER_CODE ||'|'|| " +
//                       "TD_SENDER_ACC_TYPE ||'|'|| TD_RECEIVER_ACC_TYPE ||'|'|| TD_CCY_CODE ||'|'|| " +
//                       "TD_SWIFT_REF ||'|'|| TD_SWIFT_MUR ||'|'|| TD_TXN_DATE ||'|'|| TD_TXN_TIME ||'|'|| " +
//                       "TD_TXN_STAT ||'|'|| TD_TXN_TYPE ||'|'|| TD_TXN_AMT ||'|'|| TD_DR_CR ||'|'|| " +
//                       "TD_QUEUE_STAT ||'|'|| TD_SWIFT_MT ||'|'|| TD_RELATED_REF ||'|'|| TD_ORDERING_INST ||'|'|| " +
//                       "TD_BENEFICIARY_INST ||'|'|| TD_CHANNEL ||'|'|| TD_RETURN_CODE ||'|'|| TD_ACC_WITH_INST ||'|'|| " +
//                       "TD_REMITT_INFO ||'|'|| TO_CHAR(TD_UPD_DT_STAMP, 'YYYYMMDDHH24MISS') ||'|'|| TD_MBRPOS_TYPE ||'|'|| TD_SENDER_TO_RECEIVER_INFO AS TD_DATA " +
//                       "FROM TXN_DETAIL WHERE TO_NUMBER(SUBSTR(TD_RTGS_REF_NO, 2)) > ? ORDER BY TD_RTGS_REF_NO) " +
//                       "WHERE ROWNUM <= ? ";                       
//            //23-Jul-2008 - Asokan - MEPSSUP000000044 - End                       
//
//            pstmt1 = conn.prepareStatement(sqlstmt1);
//         
//            while(contflag)
//            {
//                RtgsException.log(0, debugString, "Checking for records ... Current count:"+totalnum);
//                pstmt1.clearParameters();
//                pstmt1.setLong(1,startseq);
//                pstmt1.setLong(2,reclmt);
//                rset1 = pstmt1.executeQuery();
//                loopnum = 0;
//                outstr = new StringBuffer();
//
//                //System.out.println("@@startseq>>"+startseq);
//                //System.out.println("@@reclmt>>"+reclmt);
//                //System.out.println("@@loopnum>>"+loopnum);
//                while(rset1.next())
//                {
//                    totalnum++;
//                    loopnum++;
//                    startseq = rset1.getLong(1);
//                    recstr   = rset1.getString(2);
//                    //msgstr     = rset1.getString(3);
//                    //outstr = outstr.append(recstr).append("|").append(msgstr).append("\r\n");
//                    outstr = outstr.append(recstr).append("\r\n");
//                }
//
//                //System.out.println("%%loopnum>>"+loopnum);
//                if (loopnum == 0)
//                {
//                    contflag = false;
//                }else
//                {
//                  curgeneral.writeToFile(Archive_Dir, dtstamp+"_"+"RTGS_fundtxn.txt", outstr.toString(), true,true);
//                }
//            }
//
//            if (totalnum > 0)
//            {
//                RtgsException.log(0, debugString, "Writing archive file for fundtxn. Total records:"+totalnum);
//            }else
//            {
//                RtgsException.log(0, debugString, "No records retrieved for fundtxn.");
//            }
//
//        }
//        catch (Exception e)
//        {
//            TECLogging.log(9,"Job","BTH0002",e.toString());
//            RtgsException.log(1, debugString, "extractTxnDetail()", e);
//            throw e;
//        }
//        finally
//        {
//            if (rset1 != null) {
//                rset1.close();
//                rset1 = null;
//            }
//            if (pstmt1 != null) {
//                pstmt1.close();
//                pstmt1 = null;
//            }
//        }
//    }  // extractTxnDetail()
//
//
//
//
//    /**
//    * Extract all record in IN_MSG_LOG
//    *
//    * @return
//    */
//    public void extractInMsgLog() throws Exception
//    {
//        PreparedStatement pstmt1 = null;
//        ResultSet rset1 = null;
//        String sqlstmt1 = null;
//        boolean contflag = true;
//        long startseq = 0;
//        int reclmt = 100;
//        long totalnum = 0;
//        long loopnum = 0;
//        String seqno="";
//        String recstr = "";
//        String msgstr = "";
//        StringBuffer outstr = null;
//        Clob clob = null;
//        General curgeneral = new General();
//        String dtstamp = curgeneral.getCurDateTime("yyyyMMddHHmmss");
//        
//        try
//        {
//            sqlstmt1 = "SELECT ML_SEQ_NO, ML_DATA, ML_MSG_IMAGE FROM " +
//                       "(SELECT ML_SEQ_NO, " +
//                       "ML_SEQ_NO ||'|'|| ML_RTGS_REF_NO ||'|'|| " +
//                       "ML_MSG_SENDER_CODE ||'|'|| ML_MSG_RECEIVER_CODE ||'|'|| ML_MSG_TYPE ||'|'|| " +
//                       "ML_TXN_DATE ||'|'|| ML_VAL_DATE ||'|'|| ML_FIELD_20 ||'|'|| " +
//                       "ML_TXN_AMT ||'|'|| " +
//                       "TO_CHAR(ML_RECEIVED_STAMP, 'YYYYMMDDHH24MISS') AS ML_DATA, " +
//                       "ML_MSG_IMAGE " +
//                       "FROM MSG_LOG WHERE ML_SEQ_NO > ? ORDER BY ML_SEQ_NO) " +
//                       "WHERE ROWNUM <= ? ";
//
//            pstmt1 = conn.prepareStatement(sqlstmt1);
//
//            while(contflag)
//            {
//                RtgsException.log(0, debugString, "Checking for records ... Current count:"+totalnum);
//                pstmt1.clearParameters();
//                pstmt1.setLong(1,startseq);
//                pstmt1.setLong(2,reclmt);
//                rset1 = pstmt1.executeQuery();
//                loopnum = 0;
//                outstr = new StringBuffer();
//
//
//                //System.out.println("##startseq>>"+startseq);
//                //System.out.println("##reclmt>>"+reclmt);
//                //System.out.println("##loopnum>>"+loopnum);
//                while(rset1.next())
//                {
//                    totalnum++;
//                    loopnum++;
//                    startseq = rset1.getLong(1);
//                    recstr   = rset1.getString(2);
//                    msgstr     = rset1.getString(3);
//                    outstr = outstr.append(recstr).append("|").append(msgstr).append("\r\n");
//                }
//
//                //System.out.println("$$loopnum>>"+loopnum);
//                if (loopnum == 0)
//                {
//                    contflag = false;
//                }else
//                {
//                  RtgsException.log(0, debugString, "Writing archive file for msgin. Total records:"+totalnum);
//                  curgeneral.writeToFile(Archive_Dir, dtstamp+"_"+"RTGS_msgin.txt", outstr.toString(), true, true);
//                }            
//            }
//
//            if (totalnum > 0)
//            {
//                RtgsException.log(0, debugString, "Writing archive file for msgin. Total records:"+totalnum);
//            }else
//            {
//                RtgsException.log(0, debugString, "No records retrieved for msgin.");
//            } 
//
//        }
//        catch (Exception e)
//        {
//            TECLogging.log(9,"Job","BTH0002",e.toString());
//            RtgsException.log(1, debugString, "extractInMsgLog()", e);
//            throw e;
//        }
//        finally
//        {
//            if (rset1 != null) {
//                rset1.close();
//                rset1 = null;
//            }
//            if (pstmt1 != null) {
//                pstmt1.close();
//                pstmt1 = null;
//            }
//        }
//    }  // extractInMsgLog()
//
//
//  //03-July-2015 - Eswari - MEPSSUP-2014-0092_2 - Start
//    /*public static void main(String[] args) throws Exception
//    {
//        try
//        {
//            //For Testing
//            DataExtractionJob prg = new DataExtractionJob();
//            prg.process();
//        }
//        catch (Exception exp)
//        {
//            throw new Exception(exp.getMessage());
//        }
//    }*/
//  //03-July-2015 - Eswari - MEPSSUP-2014-0092_2 - End
//
}

