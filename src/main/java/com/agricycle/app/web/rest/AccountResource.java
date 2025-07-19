package com.agricycle.app.web.rest;

import com.agricycle.app.domain.Agriculteur;
import com.agricycle.app.domain.Commercant;
import com.agricycle.app.domain.Consommateur;
import com.agricycle.app.domain.Entreprise;
import com.agricycle.app.domain.Organisation;
import com.agricycle.app.domain.Transporteur;
import com.agricycle.app.domain.User;
import com.agricycle.app.domain.Utilisateur;
import com.agricycle.app.repository.AgriculteurRepository;
import com.agricycle.app.repository.CommercantRepository;
import com.agricycle.app.repository.ConsommateurRepository;
import com.agricycle.app.repository.EntrepriseRepository;
import com.agricycle.app.repository.OrganisationRepository;
import com.agricycle.app.repository.TransporteurRepository;
import com.agricycle.app.repository.UserRepository;
import com.agricycle.app.repository.UtilisateurRepository;
import com.agricycle.app.security.SecurityUtils;
import com.agricycle.app.service.MailService;
import com.agricycle.app.service.UserService;
import com.agricycle.app.service.dto.AdminUserDTO;
import com.agricycle.app.service.dto.PasswordChangeDTO;
import com.agricycle.app.web.rest.errors.*;
import com.agricycle.app.web.rest.vm.KeyAndPasswordVM;
import com.agricycle.app.web.rest.vm.ManagedUserVM;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private static class AccountResourceException extends RuntimeException {

        private AccountResourceException(String message) {
            super(message);
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(AccountResource.class);

    private final UserRepository userRepository;

    private final UserService userService;

    private final MailService mailService;

    private final UtilisateurRepository utilisateurRepository;

    private final AgriculteurRepository agriculteurRepository;

    private final CommercantRepository commercantRepository;

    private final TransporteurRepository transporteurRepository;

    private final ConsommateurRepository consommateurRepository;

    private final OrganisationRepository organisationRepository;

    private final EntrepriseRepository entrepriseRepository;

    public AccountResource(
        UserRepository userRepository,
        UserService userService,
        MailService mailService,
        UtilisateurRepository utilisateurRepository,
        AgriculteurRepository agriculteurRepository,
        CommercantRepository commercantRepository,
        TransporteurRepository transporteurRepository,
        ConsommateurRepository consommateurRepository,
        OrganisationRepository organisationRepository,
        EntrepriseRepository entrepriseRepository
    ) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
        this.utilisateurRepository = utilisateurRepository;
        this.agriculteurRepository = agriculteurRepository;
        this.commercantRepository = commercantRepository;
        this.transporteurRepository = transporteurRepository;
        this.consommateurRepository = consommateurRepository;
        this.organisationRepository = organisationRepository;
        this.entrepriseRepository = entrepriseRepository;
    }

    /**
     * {@code POST  /register} : register the user.
     *
     * @param managedUserVM the managed user View Model.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already used.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
        if (isPasswordLengthInvalid(managedUserVM.getPassword())) {
            throw new InvalidPasswordException();
        }
        User user = userService.registerUser(managedUserVM, managedUserVM.getPassword());
        userRepository.saveAndFlush(user);
        mailService.sendActivationEmail(user);
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setPhone(managedUserVM.getPhone());
        utilisateur.setRole(managedUserVM.getRole());
        utilisateur.setDateInscription(Instant.now());
        utilisateur.setUser(user);
        utilisateurRepository.saveAndFlush(utilisateur);

        Map<String, Object> details = managedUserVM.getProfilDetails();

        switch (managedUserVM.getRole()) {
            case AGRICULTEUR -> {
                Agriculteur agri = new Agriculteur();
                agri.setUtilisateur(utilisateur);
                agriculteurRepository.save(agri);
            }
            case COMMERCANT -> {
                Commercant c = new Commercant();
                c.setUtilisateur(utilisateur);
                commercantRepository.save(c);
            }
            case TRANSPORTEUR -> {
                Transporteur t = new Transporteur();
                t.setUtilisateur(utilisateur);
                transporteurRepository.save(t);
            }
            case CONSOMMATEUR -> {
                Consommateur c = new Consommateur();
                c.setUtilisateur(utilisateur);
                consommateurRepository.save(c);
            }
            case ORGANISATION -> {
                Organisation org = new Organisation();
                org.setUtilisateur(utilisateur);
                organisationRepository.save(org);
            }
            case ENTREPRISE -> {
                Entreprise e = new Entreprise();
                e.setUtilisateur(utilisateur);
                entrepriseRepository.save(e);
            }
            default -> throw new IllegalArgumentException("Rôle métier non reconnu");
        }
    }

    /**
     * {@code GET  /activate} : activate the registered user.
     *
     * @param key the activation key.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be activated.
     */
    @GetMapping("/activate")
    public void activateAccount(@RequestParam(value = "key") String key) {
        Optional<User> user = userService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this activation key");
        }
    }

    /**
     * {@code GET  /account} : get the current user.
     *
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @GetMapping("/account")
    public AdminUserDTO getAccount() {
        return userService
            .getUserWithAuthorities()
            .map(AdminUserDTO::new)
            .orElseThrow(() -> new AccountResourceException("User could not be found"));
    }

    /**
     * {@code POST  /account} : update the current user information.
     *
     * @param userDTO the current user information.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user login wasn't found.
     */
    @PostMapping("/account")
    public void saveAccount(@Valid @RequestBody AdminUserDTO userDTO) {
        String userLogin = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new AccountResourceException("Current user login not found"));
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.orElseThrow().getLogin().equalsIgnoreCase(userLogin))) {
            throw new EmailAlreadyUsedException();
        }
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        if (!user.isPresent()) {
            throw new AccountResourceException("User could not be found");
        }
        userService.updateUser(
            userDTO.getFirstName(),
            userDTO.getLastName(),
            userDTO.getEmail(),
            userDTO.getLangKey(),
            userDTO.getImageUrl()
        );
    }

    /**
     * {@code POST  /account/change-password} : changes the current user's password.
     *
     * @param passwordChangeDto current and new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the new password is incorrect.
     */
    @PostMapping(path = "/account/change-password")
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        if (isPasswordLengthInvalid(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
    }

    /**
     * {@code POST   /account/reset-password/init} : Send an email to reset the password of the user.
     *
     * @param mail the mail of the user.
     */
    @PostMapping(path = "/account/reset-password/init")
    public void requestPasswordReset(@RequestBody String mail) {
        Optional<User> user = userService.requestPasswordReset(mail);
        if (user.isPresent()) {
            mailService.sendPasswordResetMail(user.orElseThrow());
        } else {
            // Pretend the request has been successful to prevent checking which emails really exist
            // but log that an invalid attempt has been made
            LOG.warn("Password reset requested for non existing mail");
        }
    }

    /**
     * {@code POST   /account/reset-password/finish} : Finish to reset the password of the user.
     *
     * @param keyAndPassword the generated key and the new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the password could not be reset.
     */
    @PostMapping(path = "/account/reset-password/finish")
    public void finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
        if (isPasswordLengthInvalid(keyAndPassword.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        Optional<User> user = userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this reset key");
        }
    }

    private static boolean isPasswordLengthInvalid(String password) {
        return (
            StringUtils.isEmpty(password) ||
            password.length() < ManagedUserVM.PASSWORD_MIN_LENGTH ||
            password.length() > ManagedUserVM.PASSWORD_MAX_LENGTH
        );
    }
}
