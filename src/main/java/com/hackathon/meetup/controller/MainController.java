package com.hackathon.meetup.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.meetup.containers.Login;
import com.hackathon.meetup.containers.NewEvent;
import com.hackathon.meetup.containers.NewUser;
import com.hackathon.meetup.domain.Event;
import com.hackathon.meetup.domain.Response;
import com.hackathon.meetup.domain.Status;
import com.hackathon.meetup.domain.User;
import com.hackathon.meetup.exceptions.BadRequestException;
import com.hackathon.meetup.exceptions.ContentNotFoundException;
import com.hackathon.meetup.exceptions.InternalServerErrorException;
import com.hackathon.meetup.exceptions.UnauthorizedException;
import com.hackathon.meetup.repository.EventRepo;
import com.hackathon.meetup.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;


@RestController
public class MainController {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    UserRepo users;

    @Autowired
    EventRepo events;

    @PostConstruct
    public void init(){
        if(users.count() < 2){
            User testAdmin = new User("David","Turk","dtgt","pass","test@test.org","8675309",true);
            users.save(testAdmin);
            User testUser = new User("Cornbread", "Cat", "corn", "cat", "meow@cat.com", "8675309", false);
            users.save(testUser);
        }
        User testAdmin = users.findAll().get(0);
        System.out.println("Admin: " + testAdmin);
        User testUser = users.findAll().get(1);
        System.out.println("User: " + testUser);
        if(events.count() == 0){
            Event newEvent = new Event(testUser, "Test Event", "Moon","Creating things", new Date(), Status.NEW);
            events.save(newEvent);
        }
        Event testEvent = events.findAll().get(0);
        System.out.println("Event: " + testEvent);
    }

    @GetMapping("/")
    public String index(){
        return "You made it!";
    }

    /**
     * Events endpoints
     */
    @CrossOrigin
    @GetMapping("/api/events")
    public String getAllEvents(){
        try {
            List<Event> allEvents = events.findAll();
            Response res = new Response<List>(allEvents);
            return objectMapper.writeValueAsString(res);
        } catch (JsonProcessingException e) {
            throw new InternalServerErrorException("Error in processing to JSON");
        }
    }

    @PostMapping("/api/events")
    public String createNewEvent(@RequestBody NewEvent eventContainer,
                                 HttpSession session){
        //TODO: Remove this when a session can be created
        session.setAttribute("userId", 1);
//        if(date == null){ throw new BadRequestException("Event date cannot be null"); }
        User user = users.findOne((int) session.getAttribute("userId"));
        if(!user.isAdmin()){ throw new UnauthorizedException("User does not have admin privileges"); }
        //TODO: Test with front-end form to get Data object working
        Event newEvent = new Event();
        newEvent.setAdmin(user);
        if(eventContainer.getName() == null){ throw new BadRequestException("Name cannot be null"); }
        newEvent.setName(eventContainer.getName());
        if(eventContainer.getLocation() == null){ throw new BadRequestException("Location cannot be null"); }
        newEvent.setLocation(eventContainer.getLocation());
        newEvent.setDescription(eventContainer.getDescription());
        newEvent.setDate(new Date()); //TODO: work with the front-end to get a real date
        newEvent.setStatus(Status.NEW);
        newEvent = events.save(newEvent);
        Response res = new Response<Event>(newEvent);
        try {
            return objectMapper.writeValueAsString(res);
        } catch (JsonProcessingException e) {
            throw new InternalServerErrorException("Error in processing response as a JSON");
        }
    }

    @CrossOrigin
    @GetMapping("/api/events/{eventId}")
    public String getEventById(@PathVariable int eventId){
        Event event = events.findOne(eventId);
        if(event == null){
            throw new ContentNotFoundException("No event matching ID");
        }
        try{
            Response res = new Response<Event>(event);
            return objectMapper.writeValueAsString(res);
        } catch( JsonProcessingException e){
            throw new InternalServerErrorException("Error in processing to JSON");
        }
    }

    @PutMapping("/api/events/{eventId}")
    public String modifyEvent(@PathVariable int eventId,
                              @RequestBody NewEvent reqEvent,
                              HttpSession session){
        //TODO: Remove this when session is working
        session.setAttribute("userId", 7);
        Event event = events.findOne(eventId);
        if(event == null){ throw new ContentNotFoundException("No event matching ID"); }
        if(event.getAdmin().getUserid() != (int) session.getAttribute("userId")){
            throw new UnauthorizedException("Only the creator of an event may modify the event");
        }
        if(reqEvent.getName() == null){ throw new BadRequestException("Name cannot be null"); }
        if(reqEvent.getLocation() == null){ throw new BadRequestException("Location cannot be null"); }
        Event modifiedEvent = new Event(event.getAdmin(), reqEvent.getName(), reqEvent.getLocation(), reqEvent.getDescription(), new Date(), event.getStatus());
        modifiedEvent.setEventId(event.getEventId());
        modifiedEvent = events.save(modifiedEvent);
        try{
            Response res = new Response<Event>(modifiedEvent);
            return objectMapper.writeValueAsString(res);
        } catch (JsonProcessingException e){
            throw new InternalServerErrorException("Error processing response to JSON");
        }
    }

    @DeleteMapping("/api/events/{eventId}")
    public void deleteEvent(@PathVariable int eventId){
        Event event = events.findOne(eventId);
        if(event == null){ throw new ContentNotFoundException("No event matching provided ID"); }
        events.delete(event);
    }

    @PostMapping("/api/events/{eventId}/start")
    public String startEvent(@PathVariable int eventId){
        Event event = events.findOne(eventId);
        if(event == null){ throw new ContentNotFoundException("No event matching provided ID"); }
        event.setStatus(Status.IN_PROGRESS);
        event = events.save(event);
        Response res = new Response<Event>(event);
        try{
            return objectMapper.writeValueAsString(res);
        } catch(JsonProcessingException e){
            throw new InternalServerErrorException("Error processing response as a JSON");
        }
    }

    @PostMapping("/api/events/{eventId}/stop")
    public String stopEvent(@PathVariable int eventId){
        Event event = events.findOne(eventId);
        if(event == null){ throw new ContentNotFoundException("No event matching provided ID"); }
        event.setStatus(Status.FINISHED);
        event = events.save(event);
        Response res = new Response<Event>(event);
        try{
            return objectMapper.writeValueAsString(res);
        } catch (JsonProcessingException e){
            throw new InternalServerErrorException("Error processing response to JSON");
        }
    }

    @PostMapping("/api/events/{eventId}/rsvp")
    public String rsvp(@PathVariable int eventId){
        return null;
    }

    /**
     * Users endpoints
     */
    @CrossOrigin
    @GetMapping("/api/users/{userId}")
    public String getUserById(@PathVariable int userId){
        User user = users.findOne(userId);
        if(user == null) { throw new ContentNotFoundException("No user matching provided ID"); }
        Response res = new Response<User>(user);
        try{
            return objectMapper.writeValueAsString(res);
        } catch (JsonProcessingException e){
            throw new InternalServerErrorException("Error in processing response to JSON");
        }
    }

    @PostMapping("/api/users/block/{userId}")
    public String blockUser(@PathVariable int userId){
        return null;
    }

    /**
     * Acquaintances endpoints
     */
    @CrossOrigin
    @GetMapping("/api/acquaintances/{userId}")
    public String getAcquaintances(@PathVariable int userId){
        return null;
    }

    @DeleteMapping("/api/acquaintances/{acqId}")
    public String deleteAcquaintance(@PathVariable int acqId){
        return null;
    }

    @PostMapping("/api/acquaintances/request/{userId}")
    public String requestAcquaintance(@PathVariable int userId){
        return null;
    }

    @CrossOrigin
    @GetMapping("/api/acquaintances/request/{userId}/incoming")
    public String getIncomingRequests(@PathVariable int userId){
        return null;
    }

    @CrossOrigin
    @GetMapping("/api/acquaintances/request/{userId}/outgoing")
    public String getOutgoingRequests(@PathVariable int userId){
        return null;
    }

    @PostMapping("/api/acquaintances/request/accept/{reqId}")
    public String acceptRequest(@PathVariable int reqId){
        return null;
    }

    @DeleteMapping("/api/acquaintances/request/{reqId}")
    public String deleteRequest(@PathVariable int reqId){
        return null;
    }

    /**
     * Notes endpoints
     */
    @PostMapping("/api/notes/{userId}")
    public String createNote(@PathVariable int userId){
        return null;
    }

    @CrossOrigin
    @GetMapping("/api/notes/{noteId}")
    public String getNoteById(@PathVariable int noteId){
        return null;
    }

    @PutMapping("/api/notes/{noteId}")
    public String updateNote(@PathVariable int noteId){
        return null;
    }

    @DeleteMapping("/api/notes/{noteId}")
    public String deleteNote(@PathVariable int noteId){
        return null;
    }

    @CrossOrigin
    @GetMapping("/api/notes/all/{userId}")
    public String getAllNotes(@PathVariable int userId){
        return null;
    }


    @PostMapping("/api/register")
    public String register(@RequestBody NewUser newUser){
        if(newUser.getFirstname() == null) {throw new BadRequestException( "First Name cannot be null" );}
        if(newUser.getLastname()== null) {throw new BadRequestException( "Last Name cannot be null" );}
        if(newUser.getUsername() == null){throw new BadRequestException( "Username cannot be null" );}
        if(newUser.getPassword() == null) {throw new BadRequestException( "Password cannot be null" );}
        if(newUser.getPhone() == null) {throw new BadRequestException( "Phone cannot be null" );}

        User finduser = users.findUserByUsername( newUser.getUsername() ) ;

        if(finduser != null){throw new BadRequestException( "UserName already exists");}

        User user = new User(newUser.getFirstname(), newUser.getLastname(), newUser.getUsername(), newUser.getPassword(),
                             newUser.getPhone(), newUser.getEmail(), newUser.isAdmin());
        users.save(user);

        try{
            Response res = new Response<User>(user);
            return objectMapper.writeValueAsString(res);
        } catch (JsonProcessingException e){
            throw new InternalServerErrorException("Registration: Error processing response to JSON");
        }
    }

    @PostMapping("/api/login")
    public void login(@RequestBody Login login, HttpSession session) {
        if (login.getUsername() == null || login.getUsername() == "") {
            throw new BadRequestException( "First Name cannot be null" );
        }
        if (login.getPassword() == null || login.getPassword() == "") {
            throw new BadRequestException( "Last Name cannot be null" );
        }

        User user = users.findUserByUsername( login.getUsername() );

        if(user == null){ throw new UnauthorizedException("No user with the username"); }

        if(user.getUsername().equals(login.getUsername()) && user.getPassword().equals(login.getPassword())){
            session.setAttribute("userId", user.getUserid());
        }
        else{
            throw new UnauthorizedException("Invalid username/password combination");
        }
    }

    @PostMapping("/api/logout")
    public void logout(HttpSession session){
        if(session.getAttribute("userId") == null){
            throw new BadRequestException( "Session not found" );
        }else{
           session.setAttribute( "userId", null );
        }
    }
}

