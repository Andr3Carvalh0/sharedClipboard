//
//  AppDelegate.swift
//  Projecto e Seminario
//
//  Created by André Carvalho on 04/03/2017.
//  Copyright © 2017 André Carvalho. All rights reserved.
//

import Cocoa

@NSApplicationMain
class AppDelegate: NSObject, NSApplicationDelegate {

    //Reserve space(NSSquareStatusItemLength = -2 for some reason...) to show our statusbar icon
    let statusItem = NSStatusBar.system().statusItem(withLength: NSSquareStatusItemLength)

    let popOver = NSPopover()
    var popOverMonitor: PopOverEvent?
    var connectionHandler: ConnectionHandler?
    

    func applicationDidFinishLaunching(_ aNotification: Notification) {
        connectionHandler = ConnectionHandler()
        
        if let button = statusItem.button {
            button.image = NSImage(named: "Statusbar")
            //
            //selector <-> Is a dictionary with pointers to functions
            //more info: https://www.bignerdranch.com/blog/hannibal-selector/
            //
            button.action = #selector(self.togglePopover(sender:))
            
        }

        popOverMonitor = PopOverEvent(mask: [.leftMouseUp, .rightMouseUp]) { [unowned self] event in
            if (self.popOver.isShown) {
                self.closePopover(sender: event)
            }
        }

        hasBeenLogged() ? showMainView() : showLoginView()

    }

    
    func hasBeenLogged() -> Bool{
        
        return false
    }
    
    func togglePopover(sender: AnyObject?) {
        popOver.isShown ? closePopover(sender: sender) : showPopover(sender: sender)
    }
    
    func showPopover(sender: AnyObject?) {
        if let button = statusItem.button {
            popOver.show(relativeTo: button.bounds, of: button, preferredEdge: NSRectEdge.minY)
            popOverMonitor?.start()
        }
    }
    
    func closePopover(sender: AnyObject?) {
        popOver.performClose(sender)
        popOverMonitor?.stop()
    }
    
    
    func showMainView(){
        popOver.contentViewController = MainViewController(nibName: "MainViewController", bundle: nil, connectionHandler: connectionHandler, appDelegate: self)
    
    }
    
    func showLoginView(){
        popOver.contentViewController = LoginViewController(nibName: "LoginViewController", bundle: nil, connectionHandler: connectionHandler, appDelegate: self)
    }
}

