package server;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class FileIds implements Serializable
{
    private static final long serialVersionUID = 1L;
    static Map<Integer, String> idToNameMap;
    static AtomicInteger idCounter;

    public static Integer addFileID(String filename)
    {
        Integer id = idCounter.getAndIncrement();
        idToNameMap.put(id, filename);
        saveState();
        return id;
    }

    public static void removeFileByName(String filename)
    {
        for (Map.Entry<Integer, String> item : idToNameMap.entrySet()) {
            if (item.getValue().equals(filename) ) {
                idToNameMap.remove(item.getKey());
            }
        }
        saveState();
    }

    public static String getFileName(Integer id){
        return idToNameMap.get(id);
    }

    public static void removeFileById(Integer id)
    {
        if(idToNameMap.remove(id) != null){
            saveState();
        }
    }

    public static String getFileById(String id)
    {
        System.out.println(idToNameMap.toString());
        return idToNameMap.get(Integer.parseInt(id));
    }

    private static Integer createID()
    {
        return idCounter.getAndIncrement();
    }

    static void init(){
        if(Files.exists(Path.of("filelist.data"))){
            try{
                idToNameMap = (Map<Integer, String>) SerializationUtils.deserialize("filelist.data");
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            idToNameMap = Collections.synchronizedMap(new HashMap<>());
        }

        int nextId = 0;

        if (!idToNameMap.isEmpty()) {
            try {
                nextId = Collections.max(idToNameMap.keySet()) + 1;
            } catch (Exception e) {
                nextId = 0;
            }
        }

        idCounter = new AtomicInteger(nextId);
    }

    private static synchronized void saveState(){
        try{
            SerializationUtils.serialize(FileIds.idToNameMap,"filelist.data");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
