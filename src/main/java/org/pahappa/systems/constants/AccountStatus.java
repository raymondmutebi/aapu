/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pahappa.systems.constants;

/**
 *
 * @author RayGdhrt
 */
public enum AccountStatus {
    /**
     * After Just a mere registration
     */
Created,

/**
 * After Email/Phone number verification
 */
Verified,

/**
 * After payment of registration fee
 */
Registered, 

/**
 * If you have a running subscription
 */
Active,

/**
 * When subscription is done
 */
Stopped,

/**
 * Blocking by admin
 */
Blocked;
}
