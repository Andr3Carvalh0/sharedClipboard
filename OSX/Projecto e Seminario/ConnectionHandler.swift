//
//  ConnectionHandler.swift
//  Projecto e Seminario
//
//  Created by André Carvalho on 04/03/2017.
//  Copyright © 2017 André Carvalho. All rights reserved.
//

import Cocoa

class ConnectionHandler: NSObject {
    //var is mutable, let is not
    
    var user: String
    var password: String
    
    override init(){
        user = ""
        password = ""
    }
    
    init(user: String, pass: String){
        self.user = user
        self.password = pass
    }
    
    //DarwinBoolean -> "historic" C type Boolean, used instead of Bool, 'cause SCIENCE!
    func login(user: String, password: String) -> DarwinBoolean{
        
        return false
    }
    
    func fetch(){
        
    }
    
    //Used to push the copied text to the server
    func push(text: String) -> Bool{
        
        return false
    }
    
    //Used to push the copied multimedia files to the server.This will check the file size first before attempting to send it over the server
    func push(uri: URL) -> Bool{
        
        return false
    }
}
