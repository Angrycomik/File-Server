package server;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class FileIds implements Serializable
{
    private static final long serialVersionUID = 1L;
    static Map<Integer, String> synchronizedNumbers;
    static AtomicInteger idCounter;

    public static Integer addFileID(String filename)
    {
        Integer id = idCounter.getAndIncrement();
        synchronizedNumbers.put(id, filename);
        return id;
    }

    public static void removeFileByName(String filename)
    {
        for (Map.Entry<Integer, String> item : synchronizedNumbers.entrySet()) {
            if (item.getValue().equals(filename) ) {
                synchronizedNumbers.remove(item.getKey());
            }
        }
    }

    public static String getFileName(Integer id){
        return synchronizedNumbers.get(id);
    }

    public static void removeFileById(Integer id)
    {
        synchronizedNumbers.remove(id);
    }

    public static String getFileById(String id)
    {
        System.out.println(synchronizedNumbers.toString());
        return synchronizedNumbers.get(Integer.parseInt(id));
    }

    private static Integer createID()
    {
        return idCounter.getAndIncrement();
    }

    static void init(){
        if(Files.exists(Path.of("filelist.data"))){
            try{
                synchronizedNumbers = (Map<Integer, String>) SerializationUtils.deserialize("filelist.data");
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            synchronizedNumbers = Collections.synchronizedMap(new HashMap<>());
        }

        int nextId = 0;

        if (!synchronizedNumbers.isEmpty()) {
            try {
                nextId = Collections.max(synchronizedNumbers.keySet()) + 1;
            } catch (Exception e) {
                nextId = 0;
            }
        }

        idCounter = new AtomicInteger(nextId);
    }
}
