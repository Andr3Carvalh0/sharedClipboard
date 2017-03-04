//
//  MainViewController.swift
//  Projecto e Seminario
//
//  Created by André Carvalho on 04/03/2017.
//  Copyright © 2017 André Carvalho. All rights reserved.
//

import Cocoa
import ITSwitch

class MainViewController: ParentViewController {

    @IBOutlet weak var enableSwitchOutlet: ITSwitch!
    @IBOutlet weak var imageSwitchOutlet: ITSwitch!
    
    let preferences = UserDefaults.standard
    
    override func viewDidLoad() {
        super.viewDidLoad()
        enableSwitchOutlet.checked = true
    }
    
    
    @IBAction func enableSwitch(_ sender: ITSwitch) {
        if(sender.checked){
            print("ON")
        }else{
            print("OFF")
        }
    }

    @IBAction func imageSwitch(_ sender: ITSwitch) {
        if(sender.checked){
            print("ON")
        }else{
            print("OFF")
        }
    }
}
