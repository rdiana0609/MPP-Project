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


public class ClientRpcWorker implements Runnable, IObserver {
    private IService server;
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;


    public ClientRpcWorker(IService server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (connected) {
            try {
                Object request = input.readObject();
                Response response = handleRequest((Request) request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }


            // why do we need this?
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error " + e);
        }
    }

    private Response handleRequest(Request request) {
        Response response = null;

        switch (request.type()) {
            case LOGIN: {
                response = solveLogin(request);
                break;
            }
            case LOGOUT: {
                response = solveLogout(request);
                break;
            }
            case GET_CONTESTS: {
                response = solveGetContests(request);
                break;
            }
            case GET_PARTICIPANTS_OF_CONTEST: {
                response = solveGetParticipantsOfContest(request);
                break;
            }
            case ADD_PARTICIPANT: {
                response = solveAddParticipant(request);
                break;
            }
        }
        return response;
    }

    private Response solveAddParticipant(Request request) {
        System.out.println("AddParticipant Request ...");

        RegistrationDTO registrationDTO = (RegistrationDTO) request.data();
        String name = registrationDTO.getName();
        Integer age = registrationDTO.getAge();
        List<Contest> selectedContests = registrationDTO.getSelectedContests();

        try {
            server.addParticipant(name, age, selectedContests);
            return new Response.Builder().type(ResponseType.OK).build();
        } catch (ComException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response solveGetParticipantsOfContest(Request request) {
        System.out.println("GetParticipantsOfContest Request ...");

        Integer contestId = (Integer) request.data();
        try {
            List<ParticipantDTO> participantDTOList = server.getParticipantsOfContest(contestId);
            return new Response.Builder().type(ResponseType.OK).data(participantDTOList).build();
        } catch (ComException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response solveGetContests(Request request) {
        System.out.println("GetContests Request ...");
        try {
            List<Contest> contests = server.getContests();
            return new Response.Builder().type(ResponseType.OK).data(contests).build();
        } catch (ComException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response solveLogout(Request request) {
        System.out.println("Logout request");

        UserDTO userDTO = (UserDTO) request.data();
        String username = userDTO.getUsername();
        try {
            server.logout(username);
            connected = false;
            return new Response.Builder().type(ResponseType.OK).data(true).build();
        } catch (ComException e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response solveLogin(Request request) {
        System.out.println("Login request ..." + request.type());
        UserDTO userDTO = (UserDTO) request.data();
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();

        try {
            Boolean loggedIn = server.login(username, password, this);
            return new Response.Builder().type(ResponseType.OK).data(loggedIn).build();
        } catch (ComException e) {
            connected = false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private void sendResponse(Response response) throws IOException {
        System.out.println("sending response " + response);
        output.writeObject(response);
        output.flush();
    }

    @Override
    public void participantAdded() {
        Response resp = new Response.Builder().type(ResponseType.UPDATE).build();
        System.out.println("Participant added");
        try {
            sendResponse(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
