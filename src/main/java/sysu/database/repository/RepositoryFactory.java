package sysu.database.repository;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class RepositoryFactory {
	
	private final static AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(sysu.database.config.MongoConfig.class); 
	
	public static AnnotationConfigApplicationContext getContext(){
		return context;
	}
	
	public static ClassMessageRepository getClassMessageRepository(){
		return context.getBean(ClassMessageRepository.class);
	}
	
	public static CommentRepository getCommentRepository(){
		return context.getBean(CommentRepository.class);
	}
	
	public static CommentWordRepository getCommentWordRepository(){
		return context.getBean(CommentWordRepository.class);
	}
	
	public static MethodRepository getMethodRepository(){
		return context.getBean(MethodRepository.class);
	}
	
	public static ClassRepository getClassRepository() {
		return context.getBean(ClassRepository.class);
	}

}
