package gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Class for requesting and receiving server lists.
 * <br/>
 * TODO modify where indicated.
 */
public class NameServerModule {

    private Collection<Listener<Collection<String[]>>> listeners =
            new ArrayList<>();

    public void getServerList(String nameServerAddress, int nameServerPort) {
        // TODO actually request the list
        System.err.println(
                "Request for server list from " + nameServerAddress + ":" +
                nameServerPort);
        // This is an example of a server list with
        // IP, port, topic, no of clients
        notifyListeners(
                Arrays.<String[]>asList(
                        new String[]{
                                "1.2.3.4", "1234", "Mat och pasta", "5"
                        },
                        new String[]{
                                "1.2.3.4", "1234", "Mat och pasta", "5"
                        },
                        new String[]{
                                "1.2.3.4", "1234", "Mat och pasta", "5"
                        },
                        new String[]{
                                "1.2.3.4", "1234", "Mat och pasta", "5"
                        }
                )
        );
    }

    /**
     * Adds a listener to be notified when a new server lit has been created.
     *
     * @param listener The listener.
     */
    public void addListener(Listener<Collection<String[]>> listener) {
        listeners.add(listener);
    }

    /**
     * Call this when a server list has been received. The entire list in
     * the GUI will be replaced.
     *
     * @param servers The new server list.
     */
    private void notifyListeners(Collection<String[]> servers) {
        listeners.forEach(l -> l.update(servers));
    }
}
