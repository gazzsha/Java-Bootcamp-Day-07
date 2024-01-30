package com.school21.form;

import com.school21.annotations.HtmlForm;
import com.school21.annotations.HtmlInput;

@HtmlForm(fileName = "user_form.html",action = "/user", method = "post")
public class UserForm {
    @HtmlInput(type = "text",name = "firstName",placeholder = "Enter First Name")
    private String fistName;

    @HtmlInput(type = "text",name = "last_name",placeholder = "Enter Last Name")
    private String lastName;

    @HtmlInput(type = "password",name = "password",placeholder = "Enter Password")
    private String password;

}
