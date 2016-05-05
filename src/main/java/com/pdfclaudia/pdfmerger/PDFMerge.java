/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pdfclaudia.pdfmerger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

/**
 *
 * @author Dragos-Alexandru
 */
public class PDFMerge {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        
        File rootFile;
        
        JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.home"), "Desktop"));
        
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return !f.getName().contains(".");
            }

            @Override
            public String getDescription() {
                return "folders";
            }
        });
        
        int result = fileChooser.showOpenDialog(null);
        PDFMergerUtility merger = new PDFMergerUtility();
        if(result == JFileChooser.APPROVE_OPTION){
            File openedFolder = fileChooser.getSelectedFile();
            File[] listFiles = openedFolder.listFiles();
            String folderPath = openedFolder.getPath();
            
            Arrays.sort(listFiles, (File o1, File o2) -> {
                int nr1 = Integer.parseInt(o1.getName().split(".pdf")[0]);
                int nr2 = Integer.parseInt(o2.getName().split(".pdf")[0]);
                
                return nr1 - nr2;
            });
            
            int index = 0;
            
            for(File auxFile:listFiles){
                try {
                    merger.addSource(auxFile);
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "Failed to open "+auxFile.getName(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
                System.out.println("Adding "+auxFile.getName()+" to merger");
            }
            merger.setDestinationFileName(folderPath+"/final.pdf");
            try {
                merger.mergeDocuments();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Failed to merge files", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        }else{
            JOptionPane.showMessageDialog(null, "No folder selected", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
}
