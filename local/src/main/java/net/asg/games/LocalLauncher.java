package net.asg.games;

import com.github.czyzby.websocket.CommonWebSockets;

import net.asg.games.game.managers.ClientManager;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelTable;
import net.asg.games.utils.PayloadUtil;
import net.asg.games.utils.YokelUtilities;

import org.pmw.tinylog.Logger;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Launches the server application.
 */
public class LocalLauncher {
    public static void main(final String... args) throws Exception {
        CommonWebSockets.initiate();
        Scanner scanner = new Scanner(System.in);

        ClientManager client = new ClientManager("localhost", 8000);
        System.out.println("Enter Player Name: ");
        YokelPlayer player = new YokelPlayer(scanner.nextLine());

        int seatNumber = 0;

        try{
            if(!client.connectToServer()) throw new Exception("Could not Authenticate.");
            client.requestPlayerRegister(player);

            // Input from player
            Logger.trace("Local Launcher Failed: ");
            boolean isRunning = client.isRunning();
            String loungeName = "Social";
            String roomName = "Eiffel Tower";

            // Input from player
            // The game logic starts here
            while (isRunning) {
                //screen.PrintScreen();
                // Get input from player and do something

                System.out.println("Enter command:");
                char input = scanner.nextLine().charAt(0);

                switch (input) {
                    case 'a':
                        client.requestLounges();
                        client.waitForOneRequest();
                        System.out.println(YokelUtilities.printYokelObjects(PayloadUtil.getAllLoungesRequest(client.getNextRequest().getPayload())));
                        break;
                    case 'p':
                        client.requestPlayers();
                        client.waitForOneRequest(30);
                        System.out.println(YokelUtilities.printYokelObjects(PayloadUtil.getAllRegisteredPlayersRequest(client.getNextRequest().getPayload())));
                        break;
                    case 'r':
                        client.requestPlayerRegister(player);
                        client.waitForOneRequest(30);
                        System.out.println(Arrays.toString(client.getNextRequest().getPayload()));
                        break;
                    case 'l':
                        System.out.println("Enter Lounge Name:");
                        //loungeName = scanner.nextLine();

                        System.out.println("Enter Room Name:");
                        //roomName = scanner.nextLine();

                        client.requestJoinRoom(player, loungeName, roomName);
                        client.waitForOneRequest(30);
                        System.out.println(Arrays.toString(client.getNextRequest().getPayload()));
                        break;
                    case 't':
                        System.out.println("Enter Lounge Name:");
                        //loungeName = scanner.nextLine();

                        System.out.println("Enter Room Name:");
                        //roomName = scanner.nextLine();

                        client.requestTables(loungeName, roomName);
                        client.waitForOneRequest(30);
                        System.out.println(Arrays.toString(client.getNextRequest().getPayload()));
                        break;
                    case 'o':
                        System.out.println("Enter Lounge Name:");
                        //loungeName = scanner.nextLine();

                        System.out.println("Enter Room Name:");
                        //roomName = scanner.nextLine();

                        client.requestLeaveRoom(player, loungeName, roomName);
                        client.waitForOneRequest(30);
                        System.out.println(Arrays.toString(client.getNextRequest().getPayload()));
                        break;
                    case 'c':
                        System.out.println("Enter Lounge Name:");
                        //loungeName = scanner.nextLine();

                        System.out.println("Enter Room Name:");
                        //roomName = scanner.nextLine();

                        client.requestCreateGame(loungeName, roomName, YokelTable.ACCESS_TYPE.PUBLIC, true);
                        client.waitForOneRequest(30);
                        System.out.println(Arrays.toString(client.getNextRequest().getPayload()));
                        break;
                    case 's':
                        System.out.println("Enter Lounge Name:");
                        //loungeName = scanner.nextLine();

                        System.out.println("Enter Room Name:");
                        //roomName = scanner.nextLine();

                        System.out.println("Enter Seat Number:");
                        seatNumber = Integer.parseInt(scanner.nextLine());

                        client.requestTableSit(player, loungeName, roomName, 1, seatNumber);
                        client.waitForOneRequest(30);
                        System.out.println(Arrays.toString(client.getNextRequest().getPayload()));
                        break;
                    case 'q':
                        isRunning = false;
                        break;
                }
            }
        } catch(Exception e){
            Logger.trace("Local Launcher Failed: ");
            e.printStackTrace();
        } finally {
            scanner.close();
            client.dispose();
        }
    }
}