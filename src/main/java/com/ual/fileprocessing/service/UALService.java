package com.ual.fileprocessing.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class UALService {

    public String processUTHFile(){

        return "";
    }

    public String processUDHExcel(){

        return "";
    }

    /**
     *
     * @param data
     * @return
     */
    public String processSpringShotExcel(List<String> data){

        data.stream().forEach(item->System.out.println(item));
        return String.valueOf(data.size());
    }

    public String processAvTechShotExcel(){

        return "";
    }
}

