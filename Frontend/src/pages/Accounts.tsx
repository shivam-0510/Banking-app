import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { Layout } from '../components/Layout';
import { apiService } from '../services/api';
import type { Account } from '../types';
import { Wallet, PlusCircle, ArrowRight } from 'lucide-react';

export const Accounts: React.FC = () => {
  const [accounts, setAccounts] = useState<Account[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    loadAccounts();
  }, []);

  const loadAccounts = async () => {
    try {
      setIsLoading(true);
      const data = await apiService.getMyAccounts();
      setAccounts(data);
    } catch (error) {
      console.error('Error loading accounts:', error);
    } finally {
      setIsLoading(false);
    }
  };

  if (isLoading) {
    return (
      <Layout>
        <div className="flex items-center justify-center min-h-screen">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
        </div>
      </Layout>
    );
  }

  return (
    <Layout>
      <div className="px-4 sm:px-0">
        <div className="sm:flex sm:items-center mb-6">
          <div className="sm:flex-auto">
            <h1 className="text-3xl font-bold text-gray-900">My Accounts</h1>
            <p className="mt-2 text-sm text-gray-600">Manage your bank accounts</p>
          </div>
          <div className="mt-4 sm:mt-0 sm:ml-16 sm:flex-none">
            <Link
              to="/accounts/create"
              className="inline-flex items-center justify-center rounded-md border border-transparent bg-primary-600 px-4 py-2 text-sm font-medium text-white shadow-sm hover:bg-primary-700"
            >
              <PlusCircle className="h-5 w-5 mr-2" />
              Create Account
            </Link>
          </div>
        </div>

        {accounts.length === 0 ? (
          <div className="bg-white shadow rounded-lg p-12 text-center">
            <Wallet className="mx-auto h-12 w-12 text-gray-400" />
            <h3 className="mt-2 text-sm font-medium text-gray-900">No accounts</h3>
            <p className="mt-1 text-sm text-gray-500">Get started by creating a new account.</p>
            <div className="mt-6">
              <Link
                to="/accounts/create"
                className="inline-flex items-center px-4 py-2 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-primary-600 hover:bg-primary-700"
              >
                <PlusCircle className="h-5 w-5 mr-2" />
                Create Account
              </Link>
            </div>
          </div>
        ) : (
          <div className="bg-white shadow overflow-hidden sm:rounded-md">
            <ul className="divide-y divide-gray-200">
              {accounts.map((account) => (
                <li key={account.id}>
                  <Link
                    to={`/accounts/${account.accountNumber}`}
                    className="block hover:bg-gray-50 transition"
                  >
                    <div className="px-4 py-4 sm:px-6">
                      <div className="flex items-center justify-between">
                        <div className="flex items-center">
                          <div className="flex-shrink-0">
                            <Wallet className="h-8 w-8 text-primary-600" />
                          </div>
                          <div className="ml-4">
                            <div className="flex items-center">
                              <p className="text-sm font-medium text-gray-900">
                                {account.accountType} Account
                              </p>
                              <span className={`ml-2 inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                                account.isActive
                                  ? 'bg-green-100 text-green-800' 
                                  : 'bg-gray-100 text-gray-800'
                              }`}>
                                {account.isActive ? 'ACTIVE' : 'INACTIVE'}
                              </span>
                            </div>
                            <p className="text-sm text-gray-500">{account.accountNumber}</p>
                          </div>
                        </div>
                        <div className="flex items-center">
                          <div className="text-right mr-4">
                            <p className="text-lg font-semibold text-gray-900">
                              ₹{parseFloat(account.balance.toString()).toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                            </p>
                            <p className="text-xs text-gray-500">Balance</p>
                          </div>
                          <ArrowRight className="h-5 w-5 text-gray-400" />
                        </div>
                      </div>
                    </div>
                  </Link>
                </li>
              ))}
            </ul>
          </div>
        )}
      </div>
    </Layout>
  );
};

