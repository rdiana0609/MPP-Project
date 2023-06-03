package contest.networking.rpcprotocol;

import contest.networking.dtos.RegistrationDTO;
import contest.domain.Domain.DTOs.ParticipantDTO;
import contest.domain.Domain.Entities.Contest;
import contest.networking.dtos.UserDTO;
import contest.services.ComException;
import contest.services.IObserver;
import contest.services.IService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class ServiceRpcProxy implements IService {
    private String host;
    private int port;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;
    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;
    private String username = null;
    private IObserver client = null;


    public ServiceRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<Response>();
    }

    public boolean login(String username, String password, IObserver client) throws ComException {
        initializeConnection();
        UserDTO userDTO = new UserDTO(username, password);
        Request request = new Request.Builder().type(RequestType.LOGIN).data(userDTO).build();
        sendRequest(request);
        Response response = readResponse();

        Boolean loggedIn = false;

        if (response.type() == ResponseType.OK) {
            loggedIn = (Boolean) response.data();
            if (loggedIn) {
                this.client = client;
                this.username = username;
                return true;
            }
        } else if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new ComException(err);
        }
        return loggedIn;
    }

    @Override
    public List<Contest> getContests() throws ComException {
        Request req = new Request.Builder().type(RequestType.GET_CONTESTS).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new ComException(err);
        }
        List<Contest> contests = (List<Contest>) response.data();

        return contests;
    }

    @Override
    public List<ParticipantDTO> getParticipantsOfContest(int contestId) throws ComException {
        Request req = new Request.Builder().type(RequestType.GET_PARTICIPANTS_OF_CONTEST).data((Integer)contestId).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new ComException(err);
        }
        List<ParticipantDTO> participantDTOList = (List<ParticipantDTO>) response.data();

        return participantDTOList;
    }

    @Override
    public void addParticipant(String name, Integer age, List<Contest> selectedContests) throws ComException {
        RegistrationDTO registrationDTO = new RegistrationDTO(name, age, selectedContests);
        Request req = new Request.Builder().type(RequestType.ADD_PARTICIPANT).data(registrationDTO).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new ComException(err);
        }
    }

    @Override
    public void logout(String notused) throws ComException {
        UserDTO userDTO = new UserDTO(username, null);
        Request req = new Request.Builder().type(RequestType.LOGOUT).data(userDTO).build();
        sendRequest(req);
        Response response = readResponse();
        closeConnection();
        username = null;
        client = null;
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new ComException(err);
        }
    }


    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(Request request) throws ComException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new ComException("Error sending object " + e);
        }

    }

    private Response readResponse() throws ComException {
        Response response = null;
        try {
            response = qresponses.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initializeConnection() throws ComException {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    System.out.println("response received " + response);

                    if (((Response)response).type() == ResponseType.UPDATE)

                        client.participantAdded();
                    else {
                        try {
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }
}
