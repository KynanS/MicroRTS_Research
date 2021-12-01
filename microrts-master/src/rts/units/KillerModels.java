package rts.units;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import org.jdom.Element;
import util.XMLWriter;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import gui.frontend.FEStatePane;
import com.opencsv.CSVWriter;


/**
* The killer models stores all the different versions of Killer that have been made in a text file.
* It also updates the current stats of Killer to be improved as time goes on.
* @author kynan
*/


public class KillerModels {
    
    /**
     * Creates a Killer Mark Table starting from scratch
     */
    public KillerModels(){
    	makeKiller();
    }
    
    UnitType killer = new UnitType();


    public void makeKiller(){

        
        Random random = new Random();
        killer.name = "Killer";            
        killer.cost = 1 + random.nextInt(3);            
        killer.hp = 1 + random.nextInt(4);            
        killer.minDamage = 1 + random.nextInt(2);
        killer.maxDamage = 1 + random.nextInt(2);
        if (killer.minDamage > killer.maxDamage){
            while (killer.minDamage > killer.maxDamage){
                killer.minDamage -= 1;
            }
        }
        killer.attackRange = 1 + random.nextInt(3);            
        killer.produceTime = 100;            
        killer.moveTime = 10;            
        killer.attackTime = 5;            
        killer.isResource = false;            
        killer.isStockpile = false;            
        killer.canHarvest = false;            
        killer.canMove = true;            
        killer.canAttack = true;            
        killer.sightRadius = 3;
    	
    }
    /**
     * Returns the Killer stored
     * @return
     */
    public UnitType getKiller(){
    	return killer;
    }

    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }
}