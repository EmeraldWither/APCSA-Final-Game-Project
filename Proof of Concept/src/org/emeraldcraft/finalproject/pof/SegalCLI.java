package org.emeraldcraft.finalproject.pof;

import java.util.Scanner;

import org.emeraldcraft.finalproject.pof.components.GameObject;
import org.emeraldcraft.finalproject.pof.utils.Logger;

public class SegalCLI extends Thread {
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            processCommand(scanner.nextLine().split(" "));
        }
    }

    public void processCommand(String[] args) {
        //hitboxes command
        if (args[0].equalsIgnoreCase("hitboxes")) {
            //check to if it for all or just a single person
            if (args.length > 1 && (args[1].equalsIgnoreCase("hide") || args[1].equalsIgnoreCase("show"))) {
                DebugValues.SHOW_HITBOXES = args[1].equalsIgnoreCase("show");
                Logger.command("hitboxes " + args[1], "OK");
                return;
            }
            Logger.command("hitboxes", "Invalid command. Usage: \"hitboxes [show/hide]");
            return;
        }
        //list command
        if(args[0].equalsIgnoreCase("list")){
            if(args.length > 1 && args[1].equalsIgnoreCase("gameobjects")){
                for(GameObject gameObject : SegalGame.getInstance().getGameObjects()){
                    Logger.command("list gameobjects", gameObject.toString());
                }
                Logger.command("list gameobjects", "OK");
                return;
            }
            if(args.length > 1 && args[1].equalsIgnoreCase("humans")){
                for(GameObject gameObject : SegalGame.getInstance().getHumans()){
                    Logger.command ("list humans", gameObject.toString());
                }
                Logger.command("list humans", "OK");
                return;
            }
        }
        //create human
        if(args[0].equalsIgnoreCase("create")){
            if(args.length > 1 && args[1].equalsIgnoreCase("human")){
                SegalGame.getInstance().createHuman();
                Logger.command("create human", "OK");
                return;
            }
        }
        Logger.command(args[0], "Invalid Command");
    }
}
