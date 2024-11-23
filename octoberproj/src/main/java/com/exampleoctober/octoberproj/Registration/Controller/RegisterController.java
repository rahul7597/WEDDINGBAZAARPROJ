package com.exampleoctober.octoberproj.Registration.Controller;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
// import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.exampleoctober.octoberproj.OTP;
import com.exampleoctober.octoberproj.ExceptionClass.RegistrationException;
import com.exampleoctober.octoberproj.ExceptionClass.Email.EmailFormatException;
import com.exampleoctober.octoberproj.ExceptionClass.Number.ALLNumberFormatException;
import com.exampleoctober.octoberproj.ExceptionClass.Password.PasswordFormatException;
import com.exampleoctober.octoberproj.Registration.RegisterEntity.RegisterEntity;
import com.exampleoctober.octoberproj.Registration.RegisterRepo.RegisterRepo;
import com.exampleoctober.octoberproj.Registration.RegisterService.RegisterService;
import com.exampleoctober.octoberproj.Security.JwtUtil;

import io.micrometer.common.lang.NonNull;

@RestController
@CrossOrigin("https://weddingbazaarproject.vercel.app")
public class RegisterController 
{
    private final RegisterService serv;
    private final RegisterRepo repo;
    private final EmailFormatException emailexp;
    private final ALLNumberFormatException numexp;
    private final PasswordFormatException passexp;
    private final JwtUtil jwtUtil;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;
    // private final AuthenticationManager authenticationManager;

    public RegisterController(BCryptPasswordEncoder passwordEncoder,RegisterService serv, RegisterRepo repo, EmailFormatException emailexp, ALLNumberFormatException numexp, PasswordFormatException passexp, JwtUtil jwtUtil) {
        this.serv = serv;
        this.repo = repo;
        this.emailexp = emailexp;
        this.numexp = numexp;
        this.passexp = passexp;
        this.jwtUtil = jwtUtil;
        // this.authenticationManager=authenticationManager;
        this.passwordEncoder=passwordEncoder;
    }

    @PostMapping("/usersave")
    public ResponseEntity<Map<String, String>> registration(@RequestBody RegisterEntity e) throws Exception 
    {   
        return serv.saveValidUser(e);
    }

   
    @GetMapping("/AuthorizedUserInfo")
    public ResponseEntity<Map<String, Object>> getalldetails(@RequestHeader("Authorization") @NonNull String token) 
    {
        // Token ko extract karein
        String extractedToken = token.replace("Bearer ", "");
        
        // Token verification code
        if (!jwtUtil.validateJwtToken(extractedToken)) 
        {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            
        }
        
        // Email ko extract karein
        String extractedEmail = jwtUtil.extractEmailFromToken(extractedToken);
        
        // User ko fetch karein
        RegisterEntity user = serv.getUserByEmail(extractedEmail);
        
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        
        RegisterEntity users=repo.findByEmail(extractedEmail);
        Map<String, Object> userss = new HashMap<>();
         userss.put("id",String.valueOf(users.getId()));
         userss.put("email",users.getEmail());
         userss.put("name",users.getName());
         userss.put("number",users.getNumber());
        return ResponseEntity.ok(userss);

    }


    
    @PostMapping("/login")
    public ResponseEntity<String> loginuser(@RequestBody RegisterEntity e) 
    {
        try {
            RegisterEntity user = null;
            
            // Password validation
            if (e.getPassword() == null || e.getPassword().trim() == "") {
                throw new PasswordFormatException("Password cannot be empty!");
            } else if (!passexp.validatePassword(e.getPassword())) {
                throw new PasswordFormatException("Invalid Password Format!");
            }
            
            // Login via email
            if (e.getEmail() != null && e.getEmail().trim() != "") 
            {
                if (!emailexp.validateEmail(e.getEmail())) 
                {
                    throw new EmailFormatException("Invalid Email Format!");
                }
    
                user = repo.findByEmail(e.getEmail());
                if (user == null) 
                {
                    throw new RegistrationException("Invalid Email or Password!");
                }
                
                // Password match
                if (!passwordEncoder.matches(e.getPassword(), user.getPassword())) {
                    throw new RegistrationException("Invalid Email or Password!");
                }
            } 
            // Login via number
            else if (e.getNumber() != null && e.getNumber().trim() != "") 
            {
                if (!numexp.validateNumber(e.getNumber())) 
                {
                    throw new ALLNumberFormatException("Invalid Number Format!");
                }
                user = repo.findByNumber(e.getNumber());
                if (user == null) 
                {
                    throw new RegistrationException("Invalid Number or Password!");
                }
                
                // Password match
                if (!passwordEncoder.matches(e.getPassword(), user.getPassword())) {
                    throw new RegistrationException("Invalid Number or Password!");
                }
            } 
            
            else 
            {
                throw new RegistrationException("Either Email or Number is required!");
            }
            
            String token = jwtUtil.generateJwtToken(user);
            if (jwtUtil.isJwtTokenExpired(token) || token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token has expired");
            }
            System.out.println("=========================================================");
            System.out.print("Your Token is =="+token);
            System.out.println("=========================================================");
            return ResponseEntity.ok("Login Successfully ! Your token is: " + token);
        } 
        catch (EmailFormatException e1) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e1.getMessage());
        } catch (ALLNumberFormatException e1) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e1.getMessage());
        } catch (RegistrationException e1) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e1.getMessage());
        }
        catch(PasswordFormatException p)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(p.getMessage());
        }
    }

//signup krne k baad jo OTP generate hogaa uss otp ko yaha match karenge agr otp shii hua to password generate kr sakenge
@PostMapping("/otpwithpassword")
public ResponseEntity<Map<String, String>> setpasswithOTP(@RequestBody RegisterEntity request) {
    try 
    {
        RegisterEntity user = repo.findByEmail(request.getEmail());
        // boolean otpMatch = passwordEncoder.matches(request.getOtp(), user.getOtp());

        if (user == null) {
            throw new RegistrationException("User not found!");
        }
        else if (request.getOtp() == null || request.getOtp().isEmpty()) {
            throw new RegistrationException("OTP is required!");
        }
        else if (user.getOtp() == null) {
            throw new RegistrationException("OTP not generated or expired!");
        } 
        // else if (!user.getOtp().equals(request.getOtp())) {
            
        //     throw new RegistrationException("Invalid OTP!");
        // }
        else  if (!passwordEncoder.matches(request.getOtp(), user.getOtp())) {
            throw new RegistrationException("OTP does not Match!");
        }
        
        // OTP expiry check
        OTP.OTPResponse otpResponse = new OTP.OTPResponse(user.getOtp(), user.getOtpExpiryTime());
        if (otpResponse.isExpired()) {
            throw new RegistrationException("OTP has expired!");
        }
        
        // Password validation
        if (request.getPassword() == null || request.getPassword().trim() == "") {
            throw new PasswordFormatException("Password cannot be empty!");
        } else if (!passexp.validatePassword(request.getPassword())) {
            throw new PasswordFormatException("Invalid Password Format!");
        }
        
        // Password encode
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        // user.setOtp(otpResponse.otp); // OTP expire
        user.setOtp(passwordEncoder.encode(otpResponse.otp));
        user.setOtpExpiryTime(otpResponse.expiryTime); // OTP expiry time reset
        user.setOtpExpiryTime(LocalDateTime.now().plusMinutes(2)); // OTP expiry time reset

        // repo.save(user);
        repo.save(user);
        
        return ResponseEntity.ok(Collections.singletonMap("message", "Password set successfully"));
    } catch (RegistrationException e1) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", e1.getMessage()));
    } catch (PasswordFormatException e1) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", e1.getMessage()));
    }
}
    //agr user forget password krnaa h to yaha se vo email k through otp generate kr password ko change kr sktaa h
    @PostMapping("/generate-otp")
    public ResponseEntity<Map<String, String>> generateOtp(@RequestBody RegisterEntity request) {
        try {
            // Email validation
            if (request.getEmail() == null || request.getEmail().trim() == "") {
                throw new RegistrationException("Email Should not be empty!");
            }
            
            // Email format validation
            if (!emailexp.validateEmail(request.getEmail())) {
                throw new EmailFormatException("Invalid Email Format!");
            }
            
            // Check if email exists in database
            RegisterEntity user = repo.findByEmail(request.getEmail());
            if (user == null) {
                throw new RegistrationException("User not found!");
            }
            
            // Check if OTP already sent in last 30 seconds
    if (user.getOtpExpiryTime() != null && java.time.Duration.between(user.getOtpExpiryTime(), LocalDateTime.now()).toSeconds() < 30) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Abhi OTP nahi ja sakti, 30 seconds baad hi dusri OTP bheji jaayegi"));
}

            // Generate OTP
            OTP otp = new OTP(javaMailSender, sender);

            OTP.OTPResponse otpResponse = otp.generateOTP(6,request.getEmail());
            System.out.println("===============================================================");
            System.out.println("Your OTP is = "+otpResponse.getOtp());
            // System.out.println(otpResponse.toString());
            System.out.println("===============================================================");
            
            // Save OTP in database
            user.setOtp(passwordEncoder.encode(otpResponse.otp));
            
            user.setOtpExpiryTime(LocalDateTime.now().plusSeconds(30)); 
            // Set expiry time to 30 seconds
            repo.saveAndFlush(user);
            
            return ResponseEntity.ok(Collections.singletonMap("message", "OTP generated successfully"));
        } catch (RegistrationException e1) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", e1.getMessage()));
        } catch (EmailFormatException e1) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", e1.getMessage()));
        }
    }
    
    


    @PostMapping("/passwordSetByEmail")
    public ResponseEntity<Map<String,String>> setpasswithEmail(@RequestBody RegisterEntity request) {
        try {
            // Email validation
            if (request.getEmail() == null || request.getEmail().trim() == "") {
                throw new RegistrationException("Email Should not be empty!");
            }
            
            // Email format validation
            if (!emailexp.validateEmail(request.getEmail())) {
                throw new EmailFormatException("Invalid Email Format!");
            }
            
            // Check if email exists in database
            RegisterEntity user = repo.findByEmail(request.getEmail());
            if (user == null) {
                throw new RegistrationException("User not found!");
            }
            
            // OTP validation
            if (request.getOtp() == null || request.getOtp().trim() == "") {
                throw new RegistrationException("OTP is required!");
            }
            
            if (!user.getOtp().equals(request.getOtp())) {
                throw new RegistrationException("Invalid OTP!");
            }
            
            // OTP expiry check
            if (user.getOtpExpiryTime() != null && LocalDateTime.now().isAfter(user.getOtpExpiryTime())) {
                throw new RegistrationException("OTP has expired!");
            }
    
            // Password validation
            if (request.getPassword() == null || request.getPassword().trim() == "") {
                throw new PasswordFormatException("Password cannot be empty!");
            } else if (!passexp.validatePassword(request.getPassword())) {
                throw new PasswordFormatException("Invalid Password Format!");
            }
            
            // Password encode
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            // user.setConfirmPassword(user.getPassword());
            user.setOtp(null); // OTP expire
            user.setOtpExpiryTime(null); // OTP expiry time reset
            repo.saveAndFlush(user);
            
            return ResponseEntity.ok(Collections.singletonMap("message", "Password set successfully"));
        } catch (RegistrationException e1) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", e1.getMessage()));
        }
        catch (EmailFormatException e1) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", e1.getMessage()));
        }
        catch (PasswordFormatException e1) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", e1.getMessage()));
        }
    }



    @GetMapping("/getbyId/{e}")
    public ResponseEntity<Map<String, Object>> getalldetails(@RequestHeader("Authorization") @NonNull String token,@PathVariable RegisterEntity e) {
        RegisterEntity user = repo.findById(e.getId()).orElse(null);
        String extractedToken = token.replace("Bearer ", "");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "User Not Found !"));
        }
        
        if (!jwtUtil.validateJwtToken(extractedToken)) 
        {
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "Token is not Valid"));
            
        }
        Map<String, Object> userss = new HashMap<>();
         userss.put("id",String.valueOf(user.getId()));
         userss.put("email",user.getEmail());
         userss.put("name",user.getName());
         userss.put("number",user.getNumber());
        return ResponseEntity.ok(userss);

    }


    // @GetMapping("/allusers")
    // public List<RegisterEntity> alluser(RegisterEntity e)
    // {
    //     return repo.findAll();
    // }
    
}