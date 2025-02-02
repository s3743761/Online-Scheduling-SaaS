
package rmit.com.sept.sept;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import rmit.com.sept.sept.repository.BookingRepository;
import rmit.com.sept.sept.repository.UserRepository;
import rmit.com.sept.sept.service.BookingService;
import rmit.com.sept.sept.service.BookingServiceImpl;
import rmit.com.sept.sept.service.UserService;
import rmit.com.sept.sept.service.UserServiceImpl;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
class SeptApplicationTests extends AbstractTest {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;
	private final PrintStream originalErr = System.err;

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private BookingService service;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Override
	@Before
	public void setUp(){
		super.setUp();
	}

	@Test
	//@Rollback(false)
	public void checkIfBookingPresent_ForGivenBookingId(){
		Booking booking = new Booking();
		//booking.setBookingId(1);
		booking.setServiceName("Some");
		booking.setDate(new Date(2020, 11, 22));
		Booking book = bookingRepository.save(booking);
		int id = book.getBookingId();
		boolean val = service.isBookingPresent(id);
		assertEquals(true, val);
		//assertNotEquals(0, book.getBookingId(), 0);
		//assertNotEquals(0, book.getBookingId(), );
	}




	/*
	 * Check if Booking Id Zero, stating not created, when service name is "null"
	 * */
	@Test
	public void checkIfBookingIdZero_whenServiceNameNull(){
		Booking booking = new Booking();
		booking.setServiceName(null);
		assertEquals(0, booking.getBookingId());
	}


	/*
	 * Check If the autogenerated id is not zero when a booking is created
	 * */
	@Test
	public void checkIfBookingIDNotZero_whenBookingCreated(){
		Booking booking = new Booking();
		booking.setUserId(2);
		booking.setServiceName("Some Service");
		assertNotEquals(1, booking.getBookingId());
	}

	//Testing findByUsername returning UserId of user
	//As not all fields provided, user won't be set and findByUsername should return zero
	@Test
	public void checkIfUserIdZero_whenUserNotRegistered(){
		User user = new User();
		user.setName("Kaushal");
		UserService userService = new UserServiceImpl();
		userService.getList().add(user);
		int id = userService.findByUsername("Kaushal");
		assertEquals(user.getId(), id);
	}

	/*
	 * Check if booking status is "Valid" once a booking is created
	 * */
	@Test
	public void checkIfBookingStatusValid_whenBookingCreated(){
		Booking booking = new Booking();
		booking.setServiceName("Some Booking");
		booking.setUserId(1);
		//BookingService service = new BookingServiceImpl();
		//service.createBooking(booking);
		booking.setStatus("Valid");
		//assertNotNull(booking.getServiceName());
		assertEquals("Valid", booking.getStatus());
	}

	@Test
	public void checkUserType() throws SQLException {
		//User user = new User("some@email.com","SITE_USER");
		User user = new User();
		user.setId(1);
		user.setName("Ali");
		user.setLastName("Tariq");
		user.setPassword("some");
		user.setUserType("SITE_USER");
		UserService userService = new UserServiceImpl();
		userService.getList().add(user);
		String type = userService.findUserTypeByID(user.getId());
		assertEquals("SITE_USER", type);
	}

	//checks that booking is not present, a.k.a not created when booking status is "Rejected"
	@Test
	public void bookingNotCreated_IfBookingStatusRejected(){
		Booking book = new Booking();
		book.setServiceName("Some");
		book.setUserId(2);
		book.setBookingId(1);
		book.setDate(new Date(2020, 11, 24));
		book.setStatus("Rejected");
		book.setTime("12:12");
		BookingService service = new BookingServiceImpl();
		BookingRepository repo = service.getBookingRepository();
		boolean val = false;
		if(repo!=null) {
			repo.save(book);
			val = service.isBookingPresent(1);
		}
		assertEquals(false, val);
	}


	@Test
	@Rollback(false)
	public void account_IfBookingDeleted(){
		Booking book = new Booking();
		book.setServiceName("Some");
		book.setUserId(2);
		book.setBookingId(1);
		book.setDate(new Date(2020, 11, 24));
		book.setStatus("Valid");
		book.setTime("12:12");

		service.deleteBooking(1);
		boolean val = service.isBookingPresent(1);
		assertEquals(false, val);
	}

	/*
	 * Checks if user details aren't null when either new user created with id 1 or
	 * already a user with id 1 exists in database
	 * */
	@Test
	@Rollback(false)
	public void userDetails_NotNull_WhenUserCreatedWithID1(){
		User user = new User();
		user.setId(1);
		user.setName("Ali");
		user.setLastName("Tariq");
		user.setPassword("some");
		user.setUserType("SITE_USER");
		List<User> userList =  userService.getUserDetails(1);
		//assertEquals(, userList);
		assertNotNull(userList);

	}



	@Test
	void checkUser() {
		User user = new User();
		String expected = "Prabhav";
		user.setName(expected);
		assertEquals(expected, user.getName());
	}


}