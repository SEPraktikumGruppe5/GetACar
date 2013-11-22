require 'test_helper'

class ShowControllerTest < ActionController::TestCase
  test "should get login" do
    get :login
    assert_response :success
  end

  test "should get register" do
    get :register
    assert_response :success
  end

  test "should get welcome" do
    get :welcome
    assert_response :success
  end

end
