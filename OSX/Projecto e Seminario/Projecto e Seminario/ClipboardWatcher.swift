//
//  ClipboardWatcher.swift
//  Projecto e Seminario
//
//  Created by André Carvalho on 05/03/2017.
//  Copyright © 2017 André Carvalho. All rights reserved.
//

import Cocoa

class ClipboardWatcher: NSObject {

    private let TIME_INTERVAL: TimeInterval = 10
    private let pasteboard = NSPasteboard.general()
    private var changeCount : Int
    private var timer: Timer?
    
    override init(){
        changeCount = pasteboard.changeCount
        super.init()
    }
    
    func startPolling () {
        timer = Timer.scheduledTimer(timeInterval: TIME_INTERVAL, target: self, selector: #selector(ClipboardWatcher.checkForChangesInPasteboard), userInfo: nil, repeats: true)
        
    }
    
    // [Swift - Timer does not invoke a private func as selector] so we add @objc (http://stackoverflow.com/a/30947182/217586)
    @objc private func checkForChangesInPasteboard() {
        if (pasteboard.changeCount != changeCount) {
            print("it changed")

        
            changeCount = pasteboard.changeCount
        }
    }
}
