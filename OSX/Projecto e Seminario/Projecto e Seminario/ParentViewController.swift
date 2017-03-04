//
//  ParentViewController.swift
//  Projecto e Seminario
//
//  Created by André Carvalho on 04/03/2017.
//  Copyright © 2017 André Carvalho. All rights reserved.
//

import Cocoa
import Alamofire // See Doc: https://github.com/Alamofire/Alamofire

class ParentViewController: NSViewController {

    var connectionHandler: ConnectionHandler
    var app: AppDelegate
    
    init?(nibName nibNameOrNil: String?, bundle nibBundleOrNil: Bundle?, connectionHandler handle: ConnectionHandler?, appDelegate delegate: AppDelegate?) {
        self.connectionHandler = handle!
        self.app = delegate!
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
        
    @IBAction func exitButton(_ sender: NSButton) {
        NSApplication.shared().terminate(self)
    }

}
