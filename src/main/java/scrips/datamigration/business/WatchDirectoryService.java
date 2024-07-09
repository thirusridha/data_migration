package scrips.datamigration.business;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.text.ParseException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WatchDirectoryService {
	private final Logger logger = LogManager.getLogger(WatchDirectoryService.class);

	@Autowired
	private ReadFileAndConvertService readFileAndConvertService;

	public void monitorDirectory() throws ParseException {

		try {
			WatchService ws = FileSystems.getDefault().newWatchService();
			Path path = Paths.get("drop");
			path.register(ws, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_CREATE);
			WatchKey key = ws.take();
			for (WatchEvent<?> event : key.pollEvents()) {
				Path directory = (Path) event.context();
				//readFileAndConvertService.readFileAndConvert(directory);
				logger.debug("Event kind:" + event.kind() + ". File affected: " + event.context() + ".");
				System.out.println("Event kind:" + event.kind() + ". File affected: " + event.context() + ".");
				key.reset();
			}
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
