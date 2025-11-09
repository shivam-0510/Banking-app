import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Layout } from '../components/Layout';
import { apiService } from '../services/api';
import type { AccountCreationRequest } from '../types';
import { toast } from 'react-toastify';
import { ArrowLeft } from 'lucide-react';
import { Link } from 'react-router-dom';

export const CreateAccount: React.FC = () => {
  const [formData, setFormData] = useState<AccountCreationRequest>({
    accountType: '',
    initialDeposit: 0,
    currency: 'INR',
  });
  const [accountTypes, setAccountTypes] = useState<string[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [loadingTypes, setLoadingTypes] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    loadAccountTypes();
  }, []);

  const loadAccountTypes = async () => {
    try {
      const types = await apiService.getAccountTypes();
      setAccountTypes(types);
    } catch (error) {
      toast.error('Failed to load account types');
    } finally {
      setLoadingTypes(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);

    try {
      // Ensure currency is always INR
      const accountData = { ...formData, currency: 'INR' };
      await apiService.createAccount(accountData);
      toast.success('Account created successfully!');
      navigate('/accounts');
    } catch (error) {
      // Error is handled by the API service
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Layout>
      <div className="px-4 sm:px-0 max-w-2xl mx-auto">
        <Link
          to="/accounts"
          className="inline-flex items-center text-sm text-gray-500 hover:text-gray-700 mb-4"
        >
          <ArrowLeft className="h-4 w-4 mr-2" />
          Back to Accounts
        </Link>

        <div className="bg-white shadow rounded-lg p-6">
          <h1 className="text-2xl font-bold text-gray-900 mb-6">Create New Account</h1>

          <form onSubmit={handleSubmit} className="space-y-6">
            <div>
              <label htmlFor="accountType" className="block text-sm font-medium text-gray-700">
                Account Type *
              </label>
              <select
                id="accountType"
                required
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-primary-500 focus:ring-primary-500 sm:text-sm"
                value={formData.accountType}
                onChange={(e) => setFormData({ ...formData, accountType: e.target.value })}
                disabled={loadingTypes}
              >
                <option value="">Select account type</option>
                {accountTypes.map((type) => (
                  <option key={type} value={type}>
                    {type}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label htmlFor="currency" className="block text-sm font-medium text-gray-700">
                Currency *
              </label>
              <input
                id="currency"
                type="text"
                required
                readOnly
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm bg-gray-50 focus:border-primary-500 focus:ring-primary-500 sm:text-sm uppercase"
                value="INR"
              />
              <p className="mt-1 text-sm text-gray-500">Indian Rupees (INR)</p>
            </div>

            <div>
              <label htmlFor="initialDeposit" className="block text-sm font-medium text-gray-700">
                Initial Deposit *
              </label>
              <input
                id="initialDeposit"
                type="number"
                required
                min="0"
                step="0.01"
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-primary-500 focus:ring-primary-500 sm:text-sm"
                placeholder="0.00"
                value={formData.initialDeposit}
                onChange={(e) => setFormData({ ...formData, initialDeposit: parseFloat(e.target.value) || 0 })}
              />
            </div>

            <div className="flex justify-end space-x-3">
              <Link
                to="/accounts"
                className="px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
              >
                Cancel
              </Link>
              <button
                type="submit"
                disabled={isLoading || loadingTypes}
                className="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-primary-600 hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500 disabled:opacity-50"
              >
                {isLoading ? 'Creating...' : 'Create Account'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </Layout>
  );
};

