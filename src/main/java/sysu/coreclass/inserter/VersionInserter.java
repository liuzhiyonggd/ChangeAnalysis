package sysu.coreclass.inserter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import sysu.coreclass.bean.ClassBean;
import sysu.database.repository.ClassRepository;
import sysu.database.repository.RepositoryFactory;
import sysu.database.repository.VersionRepository;
import sysu.web.bean.Version;

public class VersionInserter {
	
	public static void insert(int versionID,String username) {
		ClassRepository classRepo = RepositoryFactory.getClassRepository();
		VersionRepository versionRepo = RepositoryFactory.getVersionRepository();
		List<ClassBean> classList = classRepo.findByVersionID(versionID);
		
		Version version = new Version();
		version.setVersionID(versionID);
		version.setUsername(username);
		version.setClassNum(classList.size());
		Date date = new Date();
		version.setUploadTime(date.getTime());
		
		 SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
		 version.setFormatUploadTime(dateFormat.format(date));
		 
		 versionRepo.insert(version);
		 
		 
	}

}
