import { useState, useEffect } from 'react';
import { useNavigate, useSearchParams, Link } from 'react-router-dom';
import { Layout } from '../components/Layout';
import { apiService } from '../services/api';
import type { TransferRequest, Account } from '../types';
import { toast } from 'react-toastify';
import { ArrowLeft, ArrowLeftRight } from 'lucide-react';

export const Transfer: React.FC = () => {
  const [searchParams] = useSearchParams();
  const accountNumber = searchParams.get('account');
  const navigate = useNavigate();
  const [formData, setFormData] = useState<TransferRequest>({
    sourceAccountNumber: accountNumber || '',
    destinationAccountNumber: '',
    amount: 0,
    description: '',
  });
  const [accounts, setAccounts] = useState<Account[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [loadingAccounts, setLoadingAccounts] = useState(true);

  useEffect(() => {
    loadAccounts();
  }, []);

  useEffect(() => {
    if (accountNumber) {
      setFormData(prev => ({ ...prev, sourceAccountNumber: accountNumber }));
    }
  }, [accountNumber]);

  const loadAccounts = async () => {
    try {
      const data = await apiService.getMyAccounts();
      setAccounts(data);
    } catch (error) {
      toast.error('Failed to load accounts');
    } finally {
      setLoadingAccounts(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (formData.sourceAccountNumber === formData.destinationAccountNumber) {
      toast.error('Source and destination accounts cannot be the same');
      return;
    }

    setIsLoading(true);

    try {
      await apiService.transfer(formData);
      toast.success('Transfer completed successfully!');
      navigate(`/accounts/${formData.sourceAccountNumber}`);
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
          to={accountNumber ? `/accounts/${accountNumber}` : '/accounts'}
          className="inline-flex items-center text-sm text-gray-500 hover:text-gray-700 mb-4"
        >
          <ArrowLeft className="h-4 w-4 mr-2" />
          Back
        </Link>

        <div className="bg-white shadow rounded-lg p-6">
          <div className="flex items-center mb-6">
            <ArrowLeftRight className="h-8 w-8 text-blue-600 mr-3" />
            <h1 className="text-2xl font-bold text-gray-900">Transfer Funds</h1>
          </div>

          <form onSubmit={handleSubmit} className="space-y-6">
            <div>
              <label htmlFor="sourceAccountNumber" className="block text-sm font-medium text-gray-700">
                From Account *
              </label>
              <select
                id="sourceAccountNumber"
                required
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-primary-500 focus:ring-primary-500 sm:text-sm"
                value={formData.sourceAccountNumber}
                onChange={(e) => setFormData({ ...formData, sourceAccountNumber: e.target.value })}
                disabled={loadingAccounts || !!accountNumber}
              >
                <option value="">Select source account</option>
                {accounts.map((account) => (
                  <option key={account.id} value={account.accountNumber}>
                    {account.accountType} - {account.accountNumber} (₹{parseFloat(account.balance.toString()).toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })})
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label htmlFor="destinationAccountNumber" className="block text-sm font-medium text-gray-700">
                To Account Number *
              </label>
              <input
                id="destinationAccountNumber"
                type="text"
                required
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-primary-500 focus:ring-primary-500 sm:text-sm"
                placeholder="Enter destination account number"
                value={formData.destinationAccountNumber}
                onChange={(e) => setFormData({ ...formData, destinationAccountNumber: e.target.value })}
              />
              <p className="mt-1 text-sm text-gray-500">
                Enter the account number to transfer funds to
              </p>
            </div>

            <div>
              <label htmlFor="amount" className="block text-sm font-medium text-gray-700">
                Amount *
              </label>
              <input
                id="amount"
                type="number"
                required
                min="0.01"
                step="0.01"
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-primary-500 focus:ring-primary-500 sm:text-sm"
                placeholder="0.00"
                value={formData.amount}
                onChange={(e) => setFormData({ ...formData, amount: parseFloat(e.target.value) || 0 })}
              />
            </div>

            <div>
              <label htmlFor="description" className="block text-sm font-medium text-gray-700">
                Description (Optional)
              </label>
              <textarea
                id="description"
                rows={3}
                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-primary-500 focus:ring-primary-500 sm:text-sm"
                placeholder="Add a description for this transfer"
                value={formData.description}
                onChange={(e) => setFormData({ ...formData, description: e.target.value })}
              />
            </div>

            <div className="flex justify-end space-x-3">
              <Link
                to={accountNumber ? `/accounts/${accountNumber}` : '/accounts'}
                className="px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
              >
                Cancel
              </Link>
              <button
                type="submit"
                disabled={isLoading || loadingAccounts}
                className="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:opacity-50"
              >
                {isLoading ? 'Processing...' : 'Transfer'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </Layout>
  );
};

