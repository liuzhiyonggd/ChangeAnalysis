package sysu.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import net.sf.json.JSONObject;
import sysu.commconsistency.inserter.ClassMessageInserter;
import sysu.commconsistency.inserter.CommentInserter;
import sysu.commconsistency.inserter.CommentWordInserter;
import sysu.common.util.AnalysisFileUtils;
import sysu.coreclass.inserter.ClassInserter;
import sysu.coreclass.inserter.VersionInserter;
import sysu.coreclass.web.service.VersionService;
import sysu.database.repository.UserRepository;
import sysu.web.bean.ChartData;
import sysu.web.bean.Data;
import sysu.web.bean.User;
import sysu.web.bean.Version;

@Controller
public class HomeController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private VersionService versionService;

	@RequestMapping("/")
	public String home(Model model, HttpServletRequest request) {

		ChartData chartData = new ChartData();
		chartData.setLabels(new ArrayList<String>());
		List<Data> datasets = new ArrayList<Data>();
		Data data = new Data();
		data.setData(new ArrayList<Double>());
		data.setBackgroundColor("rgba(151,187,205,1)");
		data.setBorderColor("rgba(151,187,205,0.8)");
		datasets.add(data);
		chartData.setDatasets(datasets);
		JSONObject jsonChartData = JSONObject.fromObject(chartData);
		model.addAttribute("chartData", jsonChartData);
		if(request.getSession().getAttribute("versionID")==null) {
			request.getSession().setAttribute("versionID", 611099378);
		}
		return "index";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(@ModelAttribute User user, Model model) {

		String username = user.getUsername();
		if (userRepository.findByUsername(username) != null) {
			User t_user = new User();
			model.addAttribute("user", t_user);
			model.addAttribute("exist_username", true);
			return "login";
		}
		List<String> roles = new ArrayList<String>();
		roles.add("ROLE_USER");
		user.setRoles(roles);
		userRepository.insert(user);
		return "login";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		model.addAttribute("exist_username", false);
		return "/register";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model) {
		if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails) {

			return "index";
		} else {
			return "login";
		}
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public void upload(@RequestParam("fileToUpload") MultipartFile file, HttpServletRequest request)
			throws IOException {

		if(file==null) {
			return;
		}
		String fileName = file.getOriginalFilename();

		// 随机生成版本号
		Random random = new Random(System.currentTimeMillis());
		int versionID = random.nextInt(Integer.MAX_VALUE);

		System.out.println("versionID:" + versionID);

		// 获取用户名，如用户未登录，则使用匿名用户名
		String username = "";
		if(!(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails)) {
			username = "anonymous";
		}else {
		    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			username = userDetails.getUsername();
		}

		// 保存上传文件
		FileUtils.writeByteArrayToFile(new File("D:/changeanalysis/temp/" + fileName), file.getBytes());

		// 解压文件
		AnalysisFileUtils.unZip("D:/changeanalysis/temp/" + fileName,
				"D:/changeanalysis/files/" + username + "/" + versionID);

		// 插入类到数据表class
		ClassInserter.insert("D:/changeanalysis/files/" + username + "/" + versionID + "/old",
				"D:/changeanalysis/files/" + username + "/" + versionID + "/new", username, versionID);

		// 插入类信息到数据表classMessage
		ClassMessageInserter.insertVersion("D:/changeanalysis/files/" + username + "/" + versionID, versionID,
				username);

		// 插入注释信息到数据表comment
		CommentInserter.insert(versionID, username);

		// 插入数据到数据表commentword
		CommentWordInserter.insert(versionID);
		
		// 插入数据到数据表version
		VersionInserter.insert(versionID, username);

		// 保存版本号信息到session，供页面跳转时获取当前用户的当前查阅版本
		request.getSession().setAttribute("versionID", versionID);
		return;

	}

	@RequestMapping("/uploadlist")
	public String uploadList(Model model) {
		// 获取用户名，如用户未登录，则使用匿名用户名
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = userDetails.getUsername();
		
		List<Version> versionList = versionService.findByUsername(username);
		model.addAttribute("versionList", versionList);
		return "/uploadlist";
	}
	
	@RequestMapping("/view")
	public String view(@RequestParam("versionID") int versionID,HttpServletRequest request) {
		request.getSession().setAttribute("versionID", versionID);
		return "redirect:/changestatistics/changestatistic";
	}

}