class User < ActiveRecord::Base
  attr_accessible :pass, :username
end
