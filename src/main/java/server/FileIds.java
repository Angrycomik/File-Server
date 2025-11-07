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

    public static synchronized Integer addFileID(String filename)
    {
        Integer id = idCounter.getAndIncrement();
        idToNameMap.put(id, filename);
        saveState();
        return id;
    }

    public static synchronized  void removeFileByName(String filename)
    {
        idToNameMap.entrySet().removeIf(entry -> entry.getValue().equals(filename));
        saveState();
    }

    public static String getFileName(Integer id){
        return idToNameMap.get(id);
    }

    public static synchronized void removeFileById(Integer id)
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

    static void init(){
        if(Files.exists(Path.of("filelist.data"))){
            try{
                @SuppressWarnings("unchecked")
                Map<Integer, String> loadedMap = (Map<Integer, String>) SerializationUtils.deserialize("filelist.data");
                idToNameMap = Collections.synchronizedMap(loadedMap);
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            idToNameMap = Collections.synchronizedMap(new HashMap<>());
        }

        int nextId = 0;

        synchronized (idToNameMap) {
            if (!idToNameMap.isEmpty()) {
                try {
                    nextId = Collections.max(idToNameMap.keySet()) + 1;
                } catch (Exception e) {
                    nextId = 0;
                }
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
