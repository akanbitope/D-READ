package com.temitope.ifc.common;

import java.io.File;
import java.io.FilenameFilter;


public class IFCFileFilter implements FilenameFilter {

    public boolean accept(File dir, String name) {
        String ext = ".ifc";
        return (name.endsWith(ext));
    }
}
