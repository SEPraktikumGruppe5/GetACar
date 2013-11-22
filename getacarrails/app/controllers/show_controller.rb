class ShowController < ApplicationController
  def login
    @fehler1 = false
    @fehler2 = false
    if params[:username] != nil 
      if User.find_by_username(params[:username]) == nil
        @fehler1 = true
      elsif User.find_by_username(params[:username]).pass == params[:pass]
        session[:username] = params[:username]
        redirect_to(:action => "welcome")
      else 
        @fehler2 = true
      end   
       
    end
  end

  def welcome
  
    if params[:logout] != nil
      session[:username] = nil
      redirect_to(:action => "login")
    end
  
    @fehler = false
    if session[:username] == nil
      @fehler = true
    else 
      @username = session[:username]
    end
  end
  
  def register
  if params[:registrieren] != nil
     @fehlerdoppeluser = false
     @fehlerNoPw = false
     @fehlerPwNichtIdentisch= false
     @fehlernouser = false
     @hatgefunkt = false 
   
     if params[:newuser] == ""
      @fehlernouser = true
      
     else
        if User.find_by_username(params[:newuser]) != nil   #in die Datenbank guggn ob der User schon existiert
              @fehlerdoppeluser = true
        elsif params[:pass] == ""
              @fehlerNoPw = true
        elsif params[:pass] != params[:passwdh]
              @fehlerPwNichtIdentisch = true
        else
              
              @hatgefunkt = true
              #user anlegen
             
        end
          
      end
  end
  
  end
  
end
