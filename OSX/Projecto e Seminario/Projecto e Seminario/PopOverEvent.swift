//
//  PopOverEvent.swift
//  Projecto e Seminario
//
//  Created by André Carvalho on 04/03/2017.
//  Copyright © 2017 André Carvalho. All rights reserved.
//
//  See: http://www.extelligentcocoa.org/status-bar-app/

import Cocoa

//Used to close application popover when click outside of it.
class PopOverEvent{
    var mask: NSEventMask
    var handler: (NSEvent?) -> ()
    var monitor: Any?
    
    init(mask: NSEventMask, handler: @escaping (NSEvent?) -> ()){
        self.mask = mask
        self.handler = handler
    }

    deinit{
        stop()
    }
    
    func start(){
        monitor = NSEvent.addGlobalMonitorForEvents(matching: mask, handler: handler)
    }
    
    func stop(){
        if monitor != nil {
            NSEvent.removeMonitor(monitor!)
            monitor = nil
        }
    }

}
