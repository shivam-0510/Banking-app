import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { Layout } from '../components/Layout';
import { apiService } from '../services/api';
import type { Account } from '../types';
import { Wallet, PlusCircle, ArrowRight, TrendingUp } from 'lucide-react';

export const Dashboard: React.FC = () => {
  const [accounts, setAccounts] = useState<Account[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [totalBalance, setTotalBalance] = useState(0);

  useEffect(() => {
    loadAccounts();
  }, []);

  const loadAccounts = async () => {
    try {
      setIsLoading(true);
      const data = await apiService.getMyAccounts();
      setAccounts(data);
      
      const total = data.reduce((sum, account) => {
        return sum + (parseFloat(account.balance.toString()) || 0);
      }, 0);
      setTotalBalance(total);
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
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900">Dashboard</h1>
          <p className="mt-2 text-sm text-gray-600">Welcome back! Here's an overview of your accounts.</p>
        </div>

        {/* Summary Cards */}
        <div className="grid grid-cols-1 gap-5 sm:grid-cols-3 mb-8">
          <div className="bg-white overflow-hidden shadow rounded-lg">
            <div className="p-5">
              <div className="flex items-center">
                <div className="flex-shrink-0">
                  <Wallet className="h-6 w-6 text-primary-600" />
                </div>
                <div className="ml-5 w-0 flex-1">
                  <dl>
                    <dt className="text-sm font-medium text-gray-500 truncate">Total Accounts</dt>
                    <dd className="text-lg font-medium text-gray-900">{accounts.length}</dd>
                  </dl>
                </div>
              </div>
            </div>
          </div>

          <div className="bg-white overflow-hidden shadow rounded-lg">
            <div className="p-5">
              <div className="flex items-center">
                <div className="flex-shrink-0">
                  <TrendingUp className="h-6 w-6 text-green-600" />
                </div>
                <div className="ml-5 w-0 flex-1">
                  <dl>
                    <dt className="text-sm font-medium text-gray-500 truncate">Total Balance</dt>
                    <dd className="text-lg font-medium text-gray-900">
                      ₹{totalBalance.toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                    </dd>
                  </dl>
                </div>
              </div>
            </div>
          </div>

          <div className="bg-white overflow-hidden shadow rounded-lg">
            <div className="p-5">
              <Link
                to="/accounts/create"
                className="flex items-center justify-center w-full px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-primary-600 hover:bg-primary-700"
              >
                <PlusCircle className="h-5 w-5 mr-2" />
                Create Account
              </Link>
            </div>
          </div>
        </div>

        {/* Accounts List */}
        <div className="bg-white shadow rounded-lg">
          <div className="px-4 py-5 sm:p-6">
            <h2 className="text-lg font-medium text-gray-900 mb-4">Your Accounts</h2>
            {accounts.length === 0 ? (
              <div className="text-center py-12">
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
              <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3">
                {accounts.map((account) => (
                  <Link
                    key={account.id}
                    to={`/accounts/${account.accountNumber}`}
                    className="relative rounded-lg border border-gray-300 bg-white px-6 py-5 shadow-sm flex items-center space-x-3 hover:border-primary-500 hover:shadow-md transition"
                  >
                    <div className="flex-1 min-w-0">
                      <div className="flex items-center justify-between">
                        <p className="text-sm font-medium text-gray-900 truncate">
                          {account.accountType}
                        </p>
                        <ArrowRight className="h-5 w-5 text-gray-400" />
                      </div>
                      <p className="text-xs text-gray-500 mt-1">{account.accountNumber}</p>
                      <p className="text-lg font-semibold text-gray-900 mt-2">
                        ₹{parseFloat(account.balance.toString()).toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                      </p>
                      <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium mt-2 ${
                        account.isActive
                          ? 'bg-green-100 text-green-800' 
                          : 'bg-gray-100 text-gray-800'
                      }`}>
                        {account.isActive ? 'ACTIVE' : 'INACTIVE'}
                      </span>
                    </div>
                  </Link>
                ))}
              </div>
            )}
          </div>
        </div>
      </div>
    </Layout>
  );
};

