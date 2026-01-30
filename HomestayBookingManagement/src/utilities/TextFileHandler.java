package utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public abstract class TextFileHandler<T> implements IFileService<T> {

    @Override
    public boolean load(List<T> list, String fileName) {
        list.clear();
        File f = new File(fileName);
        if (!f.exists()) {
            return false;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    T obj = parseLine(line);
                    if (obj != null) {
                        list.add(obj);
                    }
                }
            }
            return true;
        } catch (IOException | NumberFormatException e) {
            ErrorHandler.logError(e);
            return false;
        }
    }

    @Override
    public boolean save(List<T> list, String fileName) {
        try (PrintWriter pw = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {
            for (T item : list) {
                pw.println(item.toString());
            }
            return true;
        } catch (IOException e) {
            ErrorHandler.logError(e);
            return false;
        }
    }

    public abstract T parseLine(String line);
}
