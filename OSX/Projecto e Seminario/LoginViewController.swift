//
//  LoginViewController.swift
//  Projecto e Seminario
//
//  Created by André Carvalho on 04/03/2017.
//  Copyright © 2017 André Carvalho. All rights reserved.
//

import Cocoa

class LoginViewController: ParentViewController {
    
    //Input
    @IBOutlet weak var emailField: NSTextField!
    @IBOutlet weak var passwordField: NSSecureTextField!

    //State UI(when the user isnt valid, wrong password)
    @IBOutlet weak var wrongUser: NSImageView!
    @IBOutlet weak var wrongPassword: NSImageView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        wrongUser.isHidden = true
        wrongPassword.isHidden = true
    }
    
    @IBAction func loginButton(_ sender: NSButton) {

        
        //self.connectionHandler.login(user: emailField.stringValue, password: passwordField.stringValue)
    }

}
