package Util;

import java.util.List;

public interface CreateTaskScheduler {

     /**xml読込*/
    void readXml (String xmlPath);

    /**xml作成*/
    void createXml(List<String> xml,String xmlPath);
}
