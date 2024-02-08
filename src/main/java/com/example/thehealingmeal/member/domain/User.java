package com.example.thehealingmeal.member.domain;

import com.example.thehealingmeal.member.dto.JoinChangeDto;
import com.example.thehealingmeal.member.execption.InvalidEmailAddressException;
import com.example.thehealingmeal.member.execption.InvalidUserException;
import com.example.thehealingmeal.menu.domain.Bookmark;
import com.example.thehealingmeal.menu.domain.SnackBookmark;
import com.example.thehealingmeal.survey.domain.Survey;
import com.example.thehealingmeal.survey.domain.SurveyResult;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@gmail\\.com$");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String loginId;

    private String password;

    private String name;

    private String email;

    private String birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Survey survey;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private SurveyResult surveyResult;

    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL)
    private List<Bookmark> Bookmarks = new ArrayList<>();

    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL)
    private List<SnackBookmark> snackBookmarks = new ArrayList<>();
    @Builder
    private User(String loginId, String password, String name, String email, String birthDate, Gender gender, String phoneNumber, Role role) {

        validateEmail(email);
        validateBirthDate(birthDate);
        validatePhoneNumber(phoneNumber);

        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }


    private void validateEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (!matcher.matches()) {
            throw new InvalidEmailAddressException();
        }
    }

    private void validateBirthDate(String birthDate) {
        if (birthDate.length() != 6) {
            throw new InvalidUserException("Date of birth must be 6 characters");
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber.length() != 11) {
            throw new InvalidUserException("Number must be 11 characters");
        }
    }

    public void update(JoinChangeDto joinChangeDto) {
        this.name = joinChangeDto.getName();
        this.email = joinChangeDto.getEmail();
        this.birthDate = joinChangeDto.getBirthDate();
        this.gender = joinChangeDto.getGender();
        this.phoneNumber = joinChangeDto.getPhoneNumber();
    }
}
