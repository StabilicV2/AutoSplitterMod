package autosplittermod;

import autosplittermod.config.AutoSplitterConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class LiveSplitClient
{
    private static final Logger LOGGER = LogManager.getLogger("autosplittermod");
    private static final int RECONNECT_INTERVAL_MS = 5000;

    private final LinkedBlockingQueue<String> commandQueue = new LinkedBlockingQueue<>();

    private Socket socket;
    private PrintWriter writer;
    private boolean connected = false;
    private boolean running = false;
    private Thread thread;

    public LiveSplitClient() {}

    public void start()
    {
        if (running) return;
        running = true;
        thread = new Thread(this::run, "autosplittermod-livesplit");
        thread.setDaemon(true);
        thread.start();
    }

    public void stop()
    {
        running = false;
        if (thread != null)
        {
            thread.interrupt();
        }
        disconnect();
    }

    public void run()
    {
        while (running)
        {
            if (!connected)
            {
                tryConnect();
                if (!connected)
                {
                    sleep(RECONNECT_INTERVAL_MS);
                    continue;
                }
            }

            try
            {
                String command = commandQueue.poll(RECONNECT_INTERVAL_MS, TimeUnit.MILLISECONDS);
                if (command != null)
                {
                    send(command);
                }
            } catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void tryConnect()
    {
        String host = (String) AutoSplitterMod.getConfigValue("connection:host");
        int port = (int) AutoSplitterMod.getConfigValue("connection:port");
        try
        {
            socket = new Socket(host, port);
            socket.setTcpNoDelay(true);
            writer = new PrintWriter(socket.getOutputStream(), true);
            connected = true;
            LOGGER.info("[AutoSplitterMod] Connected to LiveSplit at {}:{}", host, port);

        } catch (IOException e)
        {
            connected = false;
            if (AutoSplitterMod.getConfig("general:debugLogging"))
            {
                LOGGER.warn("[AutoSplitterMod] Failed to connect to LiveSplit: {}", e.getMessage());
            }
        }
    }

    private void send(String command)
    {
        try
        {
            writer.print(command + "\r\n");
            writer.flush();
            if (AutoSplitterMod.getConfig("general:debugLogging"))
            {
                LOGGER.info("[AutoSplitterMod] Sent to LiveSplit: {}", command);
            }
            if (writer.checkError())
            {
                throw new IOException("PrintWriter error after send");
            }
        } catch (IOException e)
        {
            LOGGER.warn("[AutoSplitterMod] Failed to send, marking connection as dead: {}]", e.getMessage());
            disconnect();
        }
    }

    private void disconnect()
    {
        connected = false;
        try
        {
            if (socket != null) socket.close();
        } catch (IOException ignored)
        {
        }
        socket = null;
        writer = null;
    }

    private void sleep(long ms)
    {
        try
        {
            Thread.sleep(ms);
        } catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }

    // Public API Commands

    public void split()
    {
        if (connected) commandQueue.offer("startorsplit");
    }

    public void reset()
    {
        if (connected) commandQueue.offer("reset");
    }

    public void startTimer()
    {
        if (connected) commandQueue.offer("starttimer");
    }

    public void pause()
    {
        if (connected) commandQueue.offer("pause");
    }

    public boolean isConnected()
    {
        return connected;
    }
}
