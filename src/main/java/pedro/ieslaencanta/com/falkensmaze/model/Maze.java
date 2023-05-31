/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pedro.ieslaencanta.com.falkensmaze.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Serializable;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import pedro.ieslaencanta.com.falkensmaze.Size;

/**
 *
 * @author Pedro
 */
@XmlRootElement
public class Maze implements Serializable{

    private Size size;
    private Block[][] blocks;
    private double time;
    private String sound;

    public Maze() {
    }

    public void init() {
        this.setBlocks(new Block[this.getSize().getHeight()][this.getSize().getWidth()]);
        for (int i = 0; i < this.getBlocks().length; i++) {
            for (int j = 0; j < this.getBlocks()[i].length; j++) {
                this.getBlocks()[i][j] = new Block();

            }
        }
    }

    public void reset() {
        for (int i = 0; i < this.getBlocks().length; i++) {
            for (int j = 0; j < this.getBlocks()[i].length; j++) {
                this.getBlocks()[i][j] = null;

            }
        }
        this.setBlocks(null);
    }

    public void reset(Size newsize) {
        this.reset();;
        this.setSize(newsize);
        this.init();
    }

    public void setBlockValue(String value, int row, int col) {
        this.getBlocks()[col][row].setValue(value);
    }

    public String getBlockValue(int row, int col) {
        return this.getBlocks()[row][col].getValue();
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public Block[][] getBlocks() {
        return blocks;
    }

    public void setBlocks(Block[][] blocks) {
        this.blocks = blocks;
    }

    public static Maze load(File file) throws IOException, ParserConfigurationException, Exception  {
        String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
      
        if(extension.equals("bin")){
          return loadBin(file); 
        }else if(extension.equals("xml")){
           return loadXML(file);
        }else if(extension.equals("json")){
            return loadJSON(file);
        }else{
           throw new Exception("Ta equivocao");
        }
    }

    public static void save(Maze maze, File file) throws Exception {
         String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
      
        if (maze.sound == null || maze.sound.equals("")) {
            throw new Exception("Es necesario indicar el sonido del laberinto");
        }
        if(extension.equals("bin")){
           saveBin(maze, file); 
        }else if(extension.equals("xml")){
            saveXML(maze, file);
        }else if(extension.equals("json")){
            saveJSON(maze, file);
        }else{
            throw new Exception("Ta's equivocao");
        }
        
        
    }

    private static Maze loadJSON(File file) throws FileNotFoundException, IOException  {
        Gson gson = new Gson();
        FileReader fr = new FileReader(file);
        Maze m = gson.fromJson(fr, Maze.class);
        
        return m;
    }

    private static Maze loadXML(File file) throws ParserConfigurationException, SAXException, IOException, JAXBException  {
        JAXBContext context = JAXBContext.newInstance( Maze.class );
        Unmarshaller unmars = context.createUnmarshaller();
        Maze m = (Maze) unmars.unmarshal(file);
        return m;
          
    }

    public static Maze loadBin(File file) throws FileNotFoundException, IOException, ClassNotFoundException  {
      FileInputStream os = new FileInputStream(file);
      
      ObjectInputStream oos = new ObjectInputStream(os);
      Maze m = (Maze) oos.readObject();
      oos.close();
      os.close();
      
      
      return m;
    }

    private static void saveJSON(Maze maze, File file) throws IOException  {
        Gson gson = new Gson();
        String json = gson.toJson(maze);
        FileWriter fw = new FileWriter(file);
        fw.write(json);
        fw.close();

    }

    private static void saveXML(Maze maze, File file) throws JAXBException  {
      JAXBContext contx = JAXBContext.newInstance(maze.getClass());
        Marshaller marsh = contx.createMarshaller();
        marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marsh.marshal(maze, file);
        
    }

    public static void saveBin(Maze maze, File file) throws FileNotFoundException, IOException  {
      FileOutputStream os = new FileOutputStream(file);
      
      ObjectOutputStream oos = new ObjectOutputStream(os);
      oos.writeObject(maze);
      oos.close();
      os.close();
    }

}
