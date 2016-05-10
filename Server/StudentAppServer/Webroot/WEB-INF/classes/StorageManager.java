
public class StorageManager {
	
	public static StudentStorage getStorage(){
		return DBStudent.getInstance();
	}
}
