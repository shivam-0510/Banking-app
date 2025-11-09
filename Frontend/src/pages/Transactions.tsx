import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { Layout } from '../components/Layout';
import { apiService } from '../services/api';
import type { Account, Transaction } from '../types';
import { ArrowDownCircle, ArrowUpCircle, ArrowLeftRight, PlusCircle } from 'lucide-react';

export const Transactions: React.FC = () => {
  const [accounts, setAccounts] = useState<Account[]>([]);
  const [allTransactions, setAllTransactions] = useState<Transaction[]>([]);
  const [selectedAccount, setSelectedAccount] = useState<string>('all');
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    loadAccounts();
  }, []);

  useEffect(() => {
    if (accounts.length > 0) {
      loadTransactions();
    }
  }, [selectedAccount, accounts]);

  const loadAccounts = async () => {
    try {
      const data = await apiService.getMyAccounts();
      setAccounts(data);
      if (data.length > 0 && selectedAccount === 'all') {
        // Load transactions for all accounts
        loadAllTransactions(data);
      }
    } catch (error) {
      console.error('Error loading accounts:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const loadAllTransactions = async (accounts: Account[]) => {
    const transactions: Transaction[] = [];
    for (const account of accounts) {
      try {
        const accountTransactions = await apiService.getAccountTransactions(account.accountNumber);
        transactions.push(...accountTransactions);
      } catch (error) {
        console.error(`Error loading transactions for account ${account.accountNumber}:`, error);
      }
    }
    // Sort by date, most recent first
    transactions.sort((a, b) => 
      new Date(b.transactionDate).getTime() - new Date(a.transactionDate).getTime()
    );
    setAllTransactions(transactions);
  };

  const loadTransactions = async () => {
    if (selectedAccount === 'all') {
      loadAllTransactions(accounts);
    } else {
      try {
        const transactions = await apiService.getAccountTransactions(selectedAccount);
        setAllTransactions(transactions);
      } catch (error) {
        console.error('Error loading transactions:', error);
      }
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
            <h1 className="text-3xl font-bold text-gray-900">Transactions</h1>
            <p className="mt-2 text-sm text-gray-600">View your transaction history</p>
          </div>
        </div>

        {/* Filter */}
        {accounts.length > 0 && (
          <div className="bg-white shadow rounded-lg p-4 mb-6">
            <label htmlFor="account" className="block text-sm font-medium text-gray-700 mb-2">
              Filter by Account
            </label>
            <select
              id="account"
              className="block w-full rounded-md border-gray-300 shadow-sm focus:border-primary-500 focus:ring-primary-500 sm:text-sm"
              value={selectedAccount}
              onChange={(e) => setSelectedAccount(e.target.value)}
            >
              <option value="all">All Accounts</option>
              {accounts.map((account) => (
                <option key={account.id} value={account.accountNumber}>
                  {account.accountType} - {account.accountNumber}
                </option>
              ))}
            </select>
          </div>
        )}

        {/* Transactions List */}
        {allTransactions.length === 0 ? (
          <div className="bg-white shadow rounded-lg p-12 text-center">
            <p className="text-gray-500">No transactions found</p>
          </div>
        ) : (
          <div className="bg-white shadow rounded-lg overflow-hidden">
            <div className="divide-y divide-gray-200">
              {allTransactions.map((transaction) => {
                const account = accounts.find(a => a.accountNumber === transaction.accountNumber);
                return (
                  <div key={transaction.id} className="px-6 py-4 hover:bg-gray-50 transition">
                    <div className="flex items-center justify-between">
                      <div className="flex items-center">
                        {transaction.transactionType === 'DEPOSIT' && (
                          <ArrowDownCircle className="h-5 w-5 text-green-600 mr-3" />
                        )}
                        {transaction.transactionType === 'WITHDRAWAL' && (
                          <ArrowUpCircle className="h-5 w-5 text-red-600 mr-3" />
                        )}
                        {transaction.transactionType === 'TRANSFER' && (
                          <ArrowLeftRight className="h-5 w-5 text-blue-600 mr-3" />
                        )}
                        <div>
                          <p className="text-sm font-medium text-gray-900">
                            {transaction.transactionType}
                          </p>
                          <p className="text-sm text-gray-500">
                            {account?.accountType || 'Account'} - {transaction.accountNumber}
                          </p>
                          <p className="text-xs text-gray-400 mt-1">
                            {new Date(transaction.transactionDate).toLocaleString()}
                          </p>
                          {transaction.description && (
                            <p className="text-xs text-gray-400 mt-1">{transaction.description}</p>
                          )}
                        </div>
                      </div>
                      <div className="text-right">
                        <p className={`text-sm font-semibold ${
                          transaction.transactionType === 'DEPOSIT' 
                            ? 'text-green-600' 
                            : transaction.transactionType === 'WITHDRAWAL'
                            ? 'text-red-600'
                            : 'text-blue-600'
                        }`}>
                          {transaction.transactionType === 'DEPOSIT' ? '+' : '-'}
                          ₹{Math.abs(parseFloat(transaction.amount.toString())).toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                        </p>
                        <span className={`inline-flex items-center px-2 py-0.5 rounded text-xs font-medium mt-1 ${
                          transaction.status === 'COMPLETED' 
                            ? 'bg-green-100 text-green-800' 
                            : transaction.status === 'PENDING'
                            ? 'bg-yellow-100 text-yellow-800'
                            : 'bg-red-100 text-red-800'
                        }`}>
                          {transaction.status}
                        </span>
                      </div>
                    </div>
                  </div>
                );
              })}
            </div>
          </div>
        )}
      </div>
    </Layout>
  );
};

