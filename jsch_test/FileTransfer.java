import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.FileInputStream;
import java.io.InputStream;

public class FileTransfer {

    public static void main(String[] args) {
        if (args.length != 5) {
            System.out.println("Uso: java -cp .:lib/jsch-0.1.55.jar FileTransfer <userB> <hostB> <passwordB> <localFile> <remoteFile>");
            System.exit(1);
        }

        String userB = args[0];
        String hostB = args[1];
        String passwordB = args[2];
        String localFile = args[3];
        String remoteFile = args[4];

        Session sessionB = null;
        ChannelSftp channelB = null;

        try {
            JSch jsch = new JSch();

            // Connect to Server B
            sessionB = jsch.getSession(userB, hostB, 22);
            sessionB.setPassword(passwordB);
            sessionB.setConfig("StrictHostKeyChecking", "no");
            sessionB.connect();

            channelB = (ChannelSftp) sessionB.openChannel("sftp");
            channelB.connect();

            // Upload file to Server B
            try (InputStream inputStream = new FileInputStream(localFile)) {
                channelB.put(inputStream, remoteFile);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channelB != null) {
                channelB.disconnect();
            }
            if (sessionB != null) {
                sessionB.disconnect();
            }
        }
    }
}

