import { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { apiService } from '../services/api';
import type { UserResponse, UserUpdateRequest } from '../types';
import { toast } from 'react-toastify';
import { X } from 'lucide-react';

interface UserProfileModalProps {
  userDetails: UserResponse | null;
  onClose: () => void;
  onUpdate: () => void;
}

export const UserProfileModal: React.FC<UserProfileModalProps> = ({
  userDetails,
  onClose,
  onUpdate,
}) => {
  const { user } = useAuth();
  const [isEditing, setIsEditing] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [formData, setFormData] = useState<UserUpdateRequest>({
    firstName: '',
    lastName: '',
    phoneNumber: '',
    address: '',
    city: '',
    state: '',
    zipCode: '',
    country: '',
  });

  useEffect(() => {
    if (userDetails) {
      setFormData({
        firstName: userDetails.firstName || '',
        lastName: userDetails.lastName || '',
        phoneNumber: userDetails.phoneNumber || '',
        address: userDetails.address || '',
        city: userDetails.city || '',
        state: userDetails.state || '',
        zipCode: userDetails.zipCode || '',
        country: userDetails.country || '',
      });
    }
  }, [userDetails]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!user?.username) return;

    setIsLoading(true);
    try {
      await apiService.updateUser(user.username, formData);
      toast.success('Profile updated successfully!');
      setIsEditing(false);
      onUpdate();
    } catch (error) {
      // Error is handled by the API service
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
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-lg shadow-xl max-w-2xl w-full max-h-[90vh] overflow-y-auto">
        <div className="sticky top-0 bg-white border-b border-gray-200 px-6 py-4 flex justify-between items-center">
          <h2 className="text-xl font-bold text-gray-900">
            {isEditing ? 'Edit Profile' : 'User Profile'}
          </h2>
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-gray-600 focus:outline-none"
          >
            <X className="w-6 h-6" />
          </button>
        </div>

        <div className="px-6 py-6">
          {/* Profile Picture */}
          <div className="flex items-center space-x-4 mb-6">
            <div className="w-20 h-20 rounded-full bg-primary-600 text-white flex items-center justify-center text-2xl font-medium">
              {userDetails?.profilePicture ? (
                <img
                  src={userDetails.profilePicture}
                  alt={getDisplayName()}
                  className="w-20 h-20 rounded-full object-cover"
                />
              ) : (
                getInitials()
              )}
            </div>
            <div>
              <h3 className="text-lg font-semibold text-gray-900">{getDisplayName()}</h3>
              <p className="text-sm text-gray-500">{userDetails?.email || user?.email}</p>
            </div>
          </div>

          {!isEditing ? (
            // View Mode
            <div className="space-y-4">
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700">First Name</label>
                  <p className="mt-1 text-sm text-gray-900">{userDetails?.firstName || 'Not set'}</p>
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700">Last Name</label>
                  <p className="mt-1 text-sm text-gray-900">{userDetails?.lastName || 'Not set'}</p>
                </div>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Email</label>
                <p className="mt-1 text-sm text-gray-900">{userDetails?.email || user?.email}</p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Phone Number</label>
                <p className="mt-1 text-sm text-gray-900">{userDetails?.phoneNumber || 'Not set'}</p>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Address</label>
                <p className="mt-1 text-sm text-gray-900">{userDetails?.address || 'Not set'}</p>
              </div>
              <div className="grid grid-cols-3 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700">City</label>
                  <p className="mt-1 text-sm text-gray-900">{userDetails?.city || 'Not set'}</p>
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700">State</label>
                  <p className="mt-1 text-sm text-gray-900">{userDetails?.state || 'Not set'}</p>
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700">Zip Code</label>
                  <p className="mt-1 text-sm text-gray-900">{userDetails?.zipCode || 'Not set'}</p>
                </div>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Country</label>
                <p className="mt-1 text-sm text-gray-900">{userDetails?.country || 'Not set'}</p>
              </div>
              <div className="pt-4 border-t border-gray-200">
                <button
                  onClick={() => setIsEditing(true)}
                  className="w-full px-4 py-2 bg-primary-600 text-white rounded-md hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-primary-500"
                >
                  Edit Profile
                </button>
              </div>
            </div>
          ) : (
            // Edit Mode
            <form onSubmit={handleSubmit} className="space-y-4">
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label htmlFor="firstName" className="block text-sm font-medium text-gray-700">
                    First Name
                  </label>
                  <input
                    type="text"
                    id="firstName"
                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-primary-500 focus:ring-primary-500 sm:text-sm"
                    value={formData.firstName}
                    onChange={(e) => setFormData({ ...formData, firstName: e.target.value })}
                  />
                </div>
                <div>
                  <label htmlFor="lastName" className="block text-sm font-medium text-gray-700">
                    Last Name
                  </label>
                  <input
                    type="text"
                    id="lastName"
                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-primary-500 focus:ring-primary-500 sm:text-sm"
                    value={formData.lastName}
                    onChange={(e) => setFormData({ ...formData, lastName: e.target.value })}
                  />
                </div>
              </div>
              <div>
                <label htmlFor="phoneNumber" className="block text-sm font-medium text-gray-700">
                  Phone Number
                </label>
                <input
                  type="tel"
                  id="phoneNumber"
                  className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-primary-500 focus:ring-primary-500 sm:text-sm"
                  value={formData.phoneNumber}
                  onChange={(e) => setFormData({ ...formData, phoneNumber: e.target.value })}
                />
              </div>
              <div>
                <label htmlFor="address" className="block text-sm font-medium text-gray-700">
                  Address
                </label>
                <textarea
                  id="address"
                  rows={2}
                  className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-primary-500 focus:ring-primary-500 sm:text-sm"
                  value={formData.address}
                  onChange={(e) => setFormData({ ...formData, address: e.target.value })}
                />
              </div>
              <div className="grid grid-cols-3 gap-4">
                <div>
                  <label htmlFor="city" className="block text-sm font-medium text-gray-700">
                    City
                  </label>
                  <input
                    type="text"
                    id="city"
                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-primary-500 focus:ring-primary-500 sm:text-sm"
                    value={formData.city}
                    onChange={(e) => setFormData({ ...formData, city: e.target.value })}
                  />
                </div>
                <div>
                  <label htmlFor="state" className="block text-sm font-medium text-gray-700">
                    State
                  </label>
                  <input
                    type="text"
                    id="state"
                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-primary-500 focus:ring-primary-500 sm:text-sm"
                    value={formData.state}
                    onChange={(e) => setFormData({ ...formData, state: e.target.value })}
                  />
                </div>
                <div>
                  <label htmlFor="zipCode" className="block text-sm font-medium text-gray-700">
                    Zip Code
                  </label>
                  <input
                    type="text"
                    id="zipCode"
                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-primary-500 focus:ring-primary-500 sm:text-sm"
                    value={formData.zipCode}
                    onChange={(e) => setFormData({ ...formData, zipCode: e.target.value })}
                  />
                </div>
              </div>
              <div>
                <label htmlFor="country" className="block text-sm font-medium text-gray-700">
                  Country
                </label>
                <input
                  type="text"
                  id="country"
                  className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-primary-500 focus:ring-primary-500 sm:text-sm"
                  value={formData.country}
                  onChange={(e) => setFormData({ ...formData, country: e.target.value })}
                />
              </div>
              <div className="pt-4 border-t border-gray-200 flex space-x-3">
                <button
                  type="button"
                  onClick={() => setIsEditing(false)}
                  className="flex-1 px-4 py-2 border border-gray-300 rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-primary-500"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  disabled={isLoading}
                  className="flex-1 px-4 py-2 bg-primary-600 text-white rounded-md hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-primary-500 disabled:opacity-50"
                >
                  {isLoading ? 'Saving...' : 'Save Changes'}
                </button>
              </div>
            </form>
          )}
        </div>
      </div>
    </div>
  );
};

