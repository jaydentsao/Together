public class Main
{
    public static void main(String[] args)
    {
        //if hosting a game run this code:
        // hostGame(2);
        // joinGame("localhost");
        // joinGame("localhost");
        //joinGame("localhost");
        //joinGame("localhost");
        //joinGame("localhost");
        joinGame("10.13.30.48");

        
        //if joining a game, run this code with host's IP address:
        //joinGame("127.0.0.1");
    }

    public static void hostGame(int numPlayers)
    {
        //start server in new thread
        new Thread()
        {
            public void run()
            {
                new Server(numPlayers);
            }
        }.start();

        //wait 2 seconds for server to start
        try{Thread.sleep(2000);}catch(Exception e){}
    }

    public static void joinGame(String ipAddress)
    {
        new Thread()
        {
            public void run()
            {
                new Display(ipAddress);
            }
        }.start();
    }
}