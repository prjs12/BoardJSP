package article.model;

public class Writer {
	private String id;
	private String name;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Writer(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public Writer() {
		// TODO Auto-generated constructor stub
	}
	
	
}
