package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Lớp xử lý file Nhị phân (Binary/Serialization). Implement IFileService để
 * đồng bộ cách gọi hàm với TextFileHandler.
 */
public class BinaryFileHandler<T> implements IFileService<T> {

    @Override
    @SuppressWarnings("unchecked")
    public boolean load(List<T> list, String fileName) {
        list.clear();
        File f = new File(fileName);

        if (!f.exists()) {
            return false;
        }

        try ( ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            // Đọc toàn bộ list object từ file
            List<T> data = (List<T>) ois.readObject();
            list.addAll(data);
            return true;
        } catch (Exception e) {
            System.err.println("Error reading binary file " + fileName + ": " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean save(List<T> list, String fileName) {
        try ( ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(list);
            return true;
        } catch (IOException e) {
            System.err.println("Error writing binary file " + fileName + ": " + e.getMessage());
            return false;
        }
    }
}
