package ca.mcgill.ecse321.eventregistration.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ca.mcgill.ecse321.eventregistration.dao.CourseRepository;
import ca.mcgill.ecse321.eventregistration.dao.EventRepository;
import ca.mcgill.ecse321.eventregistration.dao.PersonRepository;
import ca.mcgill.ecse321.eventregistration.dao.RegistrationRepository;
import ca.mcgill.ecse321.eventregistration.dao.TutorRepository;
import ca.mcgill.ecse321.eventregistration.model.Course;
import ca.mcgill.ecse321.eventregistration.model.Event;
import ca.mcgill.ecse321.eventregistration.model.Person;
import ca.mcgill.ecse321.eventregistration.model.Tutor;
import ca.mcgill.ecse321.eventregistration.service.EventRegistrationService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestEventRegistrationService {

	@Autowired
	private EventRegistrationService service;

	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private TutorRepository tutorRepository;
	@Autowired
	private CourseRepository courseRepository;
	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private RegistrationRepository registrationRepository;

	@After
	public void clearDatabase() {
		// First, we clear registrations to avoid exceptions due to inconsistencies
		registrationRepository.deleteAll();
		// Then we can clear the other tables
		personRepository.deleteAll();
		tutorRepository.deleteAll();
		courseRepository.deleteAll();
		eventRepository.deleteAll();
	}

	@Test
	public void testCreatePerson() {
		assertEquals(0, service.getAllPersons().size());

		String name = "Oscar";
		String email = "oscar@helloworld.com";
		String password = "123";
		String ID = "123321";
		boolean isRemoved = false;

		try {
			service.createPerson(name, email, password, ID, isRemoved);
		} catch (IllegalArgumentException e) {
			// Check that no error occurred
			fail();
		}

		List<Person> allPersons = service.getAllPersons();

		assertEquals(1, allPersons.size());
		assertEquals(name, allPersons.get(0).getName());
	}
	@Test
	public void testCreateTutor() {
		assertEquals(0, service.getAllTutors().size());

		String name = "Oscar";
		String email = "oscar@helloworld.com";
		String password = "123";
		String ID = "123321";
		boolean isRemoved = false;
		String availability = "Monday-Friday";
		double hourlyRate = 16.5;
		boolean isVerified = true;

		try {
			service.createTutor(name, email, password, ID, isRemoved, availability, isVerified, hourlyRate);
		} catch (IllegalArgumentException e) {
			// Check that no error occurred
			fail();
		}

		List<Tutor> allPersons = service.getAllTutors();

		assertEquals(1, allPersons.size());
		assertEquals(name, allPersons.get(0).getName());
	}
	@Test
	public void testCreateTutorNull() {
		assertEquals(0, service.getAllTutors().size());

		String name = null;
		String email = null;
		String password = null;
		String ID = null;
		String availability = null;
		double hourlyRate = 0;
		boolean isVerified = false;
		String error = null;

		try {
			service.createTutor(name, email, password, ID, false, availability, isVerified, hourlyRate);
		} catch (IllegalArgumentException e) {
			error = e.getMessage();
		}

		// check error
		assertEquals("Person name cannot be empty!", error);

		// check no change in memory
		assertEquals(0, service.getAllTutors().size());

	}
	
	@Test
	public void testCreateCourse() {
		assertEquals(0, service.getAllCourses().size());

		int number = 123;


		try {
			service.createCourse(number);
		} catch (IllegalArgumentException e) {
			// Check that no error occurred
			fail();
		}

		List<Course> allCourses = service.getAllCourses();

		assertEquals(1, allCourses.size());
	}
	@Test
	public void testCreateCourseNull() {
		assertEquals(0, service.getAllCourses().size());

		int number = 0;
		String error = null;

		try {
			service.createCourse(number);
		} catch (IllegalArgumentException e) {
			error = e.getMessage();
		}

		// check error
		assertEquals("Course number cannot be 0!", error);

		// check no change in memory
		assertEquals(0, service.getAllCourses().size());

	}

	@Test
	public void testCreatePersonNull() {
		assertEquals(0, service.getAllPersons().size());

		String name = null;
		String email = null;
		String password = null;
		String ID = null;
		String error = null;

		try {
			service.createPerson(name, email, password, ID, false);
		} catch (IllegalArgumentException e) {
			error = e.getMessage();
		}

		// check error
		assertEquals("Person name cannot be empty!", error);

		// check no change in memory
		assertEquals(0, service.getAllPersons().size());

	}

	@Test
	public void testCreatePersonEmpty() {
		assertEquals(0, service.getAllPersons().size());

		String name = "";
		String email = "";
		String password = "";
		String ID = "";
		String error = null;

		try {
			service.createPerson(name, email, password, ID, false);
		} catch (IllegalArgumentException e) {
			error = e.getMessage();
		}

		// check error
		assertEquals("Person name cannot be empty!", error);

		// check no change in memory
		assertEquals(0, service.getAllPersons().size());

	}

	@Test
	public void testCreatePersonSpaces() {
		assertEquals(0, service.getAllPersons().size());

		String name = " ";
		String email = " ";
		String password = " ";
		String ID = " ";
		String error = null;

		try {
			service.createPerson(name, email, password, ID, false);
		} catch (IllegalArgumentException e) {
			error = e.getMessage();
		}

		// check error
		assertEquals("Person name cannot be empty!", error);

		// check no change in memory
		assertEquals(0, service.getAllPersons().size());

	}

	@Test
	public void testCreateEvent() {
		assertEquals(0, service.getAllEvents().size());

		String name = "Soccer Game";
		Calendar c = Calendar.getInstance();
		c.set(2017, Calendar.MARCH, 16, 9, 0, 0);
		Date eventDate = new Date(c.getTimeInMillis());
		LocalTime startTime = LocalTime.parse("09:00");
		c.set(2017, Calendar.MARCH, 16, 10, 30, 0);
		LocalTime endTime = LocalTime.parse("10:30");

		try {
			service.createEvent(name, eventDate, Time.valueOf(startTime) , Time.valueOf(endTime));
		} catch (IllegalArgumentException e) {
			fail();
		}

		checkResultEvent(name, eventDate, startTime, endTime);
	}

	private void checkResultEvent(String name, Date eventDate, LocalTime startTime, LocalTime endTime) {
		assertEquals(0, service.getAllPersons().size());
		assertEquals(1, service.getAllEvents().size());
		assertEquals(name, service.getAllEvents().get(0).getName());
		assertEquals(eventDate.toString(), service.getAllEvents().get(0).getDate().toString());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		assertEquals(startTime.format(formatter).toString(), service.getAllEvents().get(0).getStartTime().toString());
		assertEquals(endTime.format(formatter).toString(), service.getAllEvents().get(0).getEndTime().toString());
		assertEquals(0, service.getAllRegistrations().size());
	}


	@Test
	public void testRegister() {
		assertEquals(0, service.getAllRegistrations().size());

		String nameP = "Oscar";
		String email = "oscar@helloworld.com";
		String password = "123";
		String ID = "123321";
		boolean isRemoved = false;

		Person person = service.createPerson(nameP, email, password, ID, isRemoved);

		assertEquals(1, service.getAllPersons().size());

		String nameE = "Soccer Game";
		Calendar c = Calendar.getInstance();
		c.set(2017, Calendar.MARCH, 16, 9, 0, 0);
		Date eventDate = new Date(c.getTimeInMillis());
		Time startTime = new Time(c.getTimeInMillis());
		c.set(2017, Calendar.MARCH, 16, 10, 30, 0);
		Time endTime = new Time(c.getTimeInMillis());
		Event event = service.createEvent(nameE, eventDate, startTime, endTime);
		assertEquals(1, service.getAllEvents().size());

		try {
			service.register(person, event);
		} catch (IllegalArgumentException e) {
			fail();
		}

		checkResultRegister(nameP, nameE, eventDate, startTime, endTime);
	}

	private void checkResultRegister(String nameP, String nameE, Date eventDate, Time startTime, Time endTime) {
		assertEquals(1, service.getAllPersons().size());
		assertEquals(nameP, service.getAllPersons().get(0).getName());
		assertEquals(1, service.getAllEvents().size());
		assertEquals(nameE, service.getAllEvents().get(0).getName());
		assertEquals(eventDate.toString(), service.getAllEvents().get(0).getDate().toString());
		assertEquals(startTime.toString(), service.getAllEvents().get(0).getStartTime().toString());
		assertEquals(endTime.toString(), service.getAllEvents().get(0).getEndTime().toString());
		assertEquals(1, service.getAllRegistrations().size());
		// Need to assert by ID (in this case: name)
		assertEquals(service.getAllEvents().get(0).getName(), service.getAllRegistrations().get(0).getEvent().getName());
		// Need to assert by ID (in this case: name)
		assertEquals(service.getAllPersons().get(0).getName(), service.getAllRegistrations().get(0).getPerson().getName());
	}


	@Test
	public void testCreateEventNull() {
		assertEquals(0, service.getAllRegistrations().size());

		String name = null;
		Date eventDate = null;
		Time startTime = null;
		Time endTime = null;

		String error = null;
		try {
			service.createEvent(name, eventDate, startTime, endTime);
		} catch (IllegalArgumentException e) {
			error = e.getMessage();
		}

		// check error
		assertEquals(
				"Event name cannot be empty! Event date cannot be empty! Event start time cannot be empty! Event end time cannot be empty!",
				error);
		// check model in memory
		assertEquals(0, service.getAllEvents().size());
	}

	@Test
	public void testCreateEventEmpty() {
		assertEquals(0, service.getAllEvents().size());

		String name = "";
		Calendar c = Calendar.getInstance();
		c.set(2017, Calendar.FEBRUARY, 16, 10, 00, 0);
		Date eventDate = new Date(c.getTimeInMillis());
		LocalTime startTime = LocalTime.parse("10:00");
		c.set(2017, Calendar.FEBRUARY, 16, 11, 30, 0);
		LocalTime endTime = LocalTime.parse("11:30");

		String error = null;
		try {
			service.createEvent(name, eventDate, Time.valueOf(startTime), Time.valueOf(endTime));
		} catch (IllegalArgumentException e) {
			error = e.getMessage();
		}

		// check error
		assertEquals("Event name cannot be empty!", error);
		// check model in memory
		assertEquals(0, service.getAllEvents().size());
	}

	@Test
	public void testCreateEventSpaces() {
		assertEquals(0, service.getAllEvents().size());

		String name = " ";
		Calendar c = Calendar.getInstance();
		c.set(2016, Calendar.OCTOBER, 16, 9, 00, 0);
		Date eventDate = new Date(c.getTimeInMillis());
		LocalTime startTime = LocalTime.parse("09:00");
		c.set(2016, Calendar.OCTOBER, 16, 10, 30, 0);
		LocalTime endTime = LocalTime.parse("10:30");

		String error = null;
		try {
			service.createEvent(name, eventDate, Time.valueOf(startTime), Time.valueOf(endTime));
		} catch (IllegalArgumentException e) {
			error = e.getMessage();
		}
		// check error
		assertEquals("Event name cannot be empty!", error);
		// check model in memory
		assertEquals(0, service.getAllEvents().size());

	}

	@Test
	public void testCreateEventEndTimeBeforeStartTime() {
		assertEquals(0, service.getAllEvents().size());

		String name = "Soccer Game";
		Calendar c = Calendar.getInstance();
		c.set(2016, Calendar.OCTOBER, 16, 9, 00, 0);
		Date eventDate = new Date(c.getTimeInMillis());
		LocalTime startTime = LocalTime.parse("09:00");
		c.set(2016, Calendar.OCTOBER, 16, 8, 59, 59);
		LocalTime endTime = LocalTime.parse("08:59");

		String error = null;
		try {
			service.createEvent(name, eventDate, Time.valueOf(startTime), Time.valueOf(endTime));
		} catch (IllegalArgumentException e) {
			error = e.getMessage();
		}

		// check error
		assertEquals("Event end time cannot be before event start time!", error);

		// check model in memory
		assertEquals(0, service.getAllEvents().size());

	}

	@Test
	public void testRegisterNull() {
		assertEquals(0, service.getAllRegistrations().size());

		Person Person = null;
		assertEquals(0, service.getAllPersons().size());

		Event event = null;
		assertEquals(0, service.getAllEvents().size());

		String error = null;
		try {
			service.register(Person, event);
		} catch (IllegalArgumentException e) {
			error = e.getMessage();
		}

		// check error
		assertEquals("Person needs to be selected for registration! Event needs to be selected for registration!",
				error);

		// check model in memory
		assertEquals(0, service.getAllRegistrations().size());
		assertEquals(0, service.getAllPersons().size());
		assertEquals(0, service.getAllEvents().size());

	}

	@Test
	public void testRegisterPersonAndEventDoNotExist() {
		assertEquals(0, service.getAllRegistrations().size());

		String nameP = "Oscar";
		Person person = new Person();
		person.setName(nameP);
		assertEquals(0, service.getAllPersons().size());

		String nameE = "Soccer Game";
		Calendar c = Calendar.getInstance();
		c.set(2016, Calendar.OCTOBER, 16, 9, 00, 0);
		Date eventDate = new Date(c.getTimeInMillis());
		Time startTime = new Time(c.getTimeInMillis());
		c.set(2016, Calendar.OCTOBER, 16, 10, 30, 0);
		Time endTime = new Time(c.getTimeInMillis());
		Event event = new Event();
		event.setName(nameE);
		event.setDate(eventDate);
		event.setStartTime(startTime);
		event.setEndTime(endTime);
		assertEquals(0, service.getAllEvents().size());

		String error = null;
		try {
			service.register(person, event);
		} catch (IllegalArgumentException e) {
			error = e.getMessage();
		}

		// check error
		assertEquals("Person does not exist! Event does not exist!", error);

		// check model in memory
		assertEquals(0, service.getAllRegistrations().size());
		assertEquals(0, service.getAllPersons().size());
		assertEquals(0, service.getAllEvents().size());

	}


}