import { useState, useEffect, useRef } from 'react';
import { useAuth } from '../context/AuthContext';
import { apiService } from '../services/api';
import type { UserResponse } from '../types';
import { User, Settings, LogOut, ChevronDown } from 'lucide-react';
import { UserProfileModal } from './UserProfileModal';

interface UserProfileDropdownProps {
  onLogout: () => void;
}

export const UserProfileDropdown: React.FC<UserProfileDropdownProps> = ({ onLogout }) => {
  const { user } = useAuth();
  const [isOpen, setIsOpen] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [userDetails, setUserDetails] = useState<UserResponse | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const dropdownRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
        setIsOpen(false);
      }
    };

    if (isOpen) {
      document.addEventListener('mousedown', handleClickOutside);
      loadUserDetails();
    }

    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, [isOpen]);

  const loadUserDetails = async () => {
    if (!user?.username) return;
    
    try {
      setIsLoading(true);
      const details = await apiService.getCurrentUser();
      setUserDetails(details);
    } catch (error) {
      console.error('Error loading user details:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const getInitials = () => {
    // Prioritize first + last name from userDetails
    if (userDetails?.firstName && userDetails?.lastName) {
      const firstInitial = userDetails.firstName.trim()[0] || '';
      const lastInitial = userDetails.lastName.trim()[0] || '';
      return `${firstInitial}${lastInitial}`.toUpperCase();
    }
    // Fallback to firstName from userDetails
    if (userDetails?.firstName) {
      return userDetails.firstName.trim()[0].toUpperCase();
    }
    // Fallback to firstName + lastName from user object
    if (user?.firstName && user?.lastName) {
      const firstInitial = user.firstName.trim()[0] || '';
      const lastInitial = user.lastName.trim()[0] || '';
      return `${firstInitial}${lastInitial}`.toUpperCase();
    }
    // Fallback to firstName from user object
    if (user?.firstName) {
      return user.firstName.trim()[0].toUpperCase();
    }
    // Final fallback to username
    if (user?.username) {
      return user.username.trim()[0].toUpperCase();
    }
    return 'U';
  };

  const getDisplayName = () => {
    if (userDetails?.firstName && userDetails?.lastName) {
      return `${userDetails.firstName} ${userDetails.lastName}`;
    }
    if (userDetails?.firstName) {
      return userDetails.firstName;
    }
    return user?.username || 'User';
  };

  return (
    <>
      <div className="relative" ref={dropdownRef}>
        <button
          onClick={() => setIsOpen(!isOpen)}
          className="flex items-center space-x-2 p-2 rounded-full hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-primary-500"
        >
          <div className="w-8 h-8 rounded-full bg-primary-600 text-white flex items-center justify-center text-sm font-medium">
            {userDetails?.profilePicture ? (
              <img
                src={userDetails.profilePicture}
                alt={getDisplayName()}
                className="w-8 h-8 rounded-full object-cover"
              />
            ) : (
              getInitials()
            )}
          </div>
          <ChevronDown className={`w-4 h-4 text-gray-600 transition-transform ${isOpen ? 'rotate-180' : ''}`} />
        </button>

        {isOpen && (
          <div className="absolute right-0 mt-2 w-64 bg-white rounded-md shadow-lg ring-1 ring-black ring-opacity-5 z-50">
            <div className="py-1">
              {/* User Info */}
              <div className="px-4 py-3 border-b border-gray-200">
                {isLoading ? (
                  <div className="flex items-center space-x-3">
                    <div className="w-10 h-10 rounded-full bg-gray-200 animate-pulse"></div>
                    <div className="flex-1">
                      <div className="h-4 bg-gray-200 rounded animate-pulse mb-2"></div>
                      <div className="h-3 bg-gray-200 rounded animate-pulse w-2/3"></div>
                    </div>
                  </div>
                ) : (
                  <div className="flex items-center space-x-3">
                    <div className="w-10 h-10 rounded-full bg-primary-600 text-white flex items-center justify-center text-sm font-medium">
                      {userDetails?.profilePicture ? (
                        <img
                          src={userDetails.profilePicture}
                          alt={getDisplayName()}
                          className="w-10 h-10 rounded-full object-cover"
                        />
                      ) : (
                        getInitials()
                      )}
                    </div>
                    <div className="flex-1 min-w-0">
                      <p className="text-sm font-medium text-gray-900 truncate">
                        {getDisplayName()}
                      </p>
                      <p className="text-xs text-gray-500 truncate">
                        {userDetails?.email || user?.email}
                      </p>
                    </div>
                  </div>
                )}
              </div>

              {/* Menu Items */}
              <button
                onClick={() => {
                  setShowModal(true);
                  setIsOpen(false);
                }}
                className="w-full flex items-center px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
              >
                <User className="w-4 h-4 mr-3 text-gray-400" />
                View Profile
              </button>
              <button
                onClick={() => {
                  setShowModal(true);
                  setIsOpen(false);
                }}
                className="w-full flex items-center px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
              >
                <Settings className="w-4 h-4 mr-3 text-gray-400" />
                Edit Profile
              </button>
              <div className="border-t border-gray-200"></div>
              <button
                onClick={() => {
                  setIsOpen(false);
                  onLogout();
                }}
                className="w-full flex items-center px-4 py-2 text-sm text-red-600 hover:bg-red-50"
              >
                <LogOut className="w-4 h-4 mr-3" />
                Logout
              </button>
            </div>
          </div>
        )}
      </div>

      {showModal && (
        <UserProfileModal
          userDetails={userDetails}
          onClose={() => {
            setShowModal(false);
            loadUserDetails();
          }}
          onUpdate={loadUserDetails}
        />
      )}
    </>
  );
};

