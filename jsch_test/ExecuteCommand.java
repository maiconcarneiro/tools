import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ExecuteCommand {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Uso: java -cp .:lib/jsch-0.1.54.jar ExecuteCommand <user> <host> <command>");
            System.exit(1);
        }

        String user = args[0];
        String host = args[1];
        String command = args[2];

        Console console = System.console();
        if (console == null) {
            System.out.println("Nenhum console disponível");
            System.exit(1);
        }
        char[] passwordArray = console.readPassword("Enter password: ");
        String password = new String(passwordArray);

        Session session = null;
        ChannelExec channel = null;

        try {
            JSch jsch = new JSch();

            // Create SSH session
            session = jsch.getSession(user, host, 22);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            // Create exec channel
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);

            // Get input stream
            InputStream input = channel.getInputStream();
            channel.connect();

            // Read command output
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
    }
}

