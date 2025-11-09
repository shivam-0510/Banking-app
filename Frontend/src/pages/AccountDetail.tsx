import { useEffect, useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { Layout } from '../components/Layout';
import { apiService } from '../services/api';
import type { Account, Transaction } from '../types';
import { ArrowLeft, DollarSign, ArrowDownCircle, ArrowUpCircle, ArrowLeftRight } from 'lucide-react';

export const AccountDetail: React.FC = () => {
  const { accountNumber } = useParams<{ accountNumber: string }>();
  const navigate = useNavigate();
  const [account, setAccount] = useState<Account | null>(null);
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    if (accountNumber) {
      loadAccount();
      loadTransactions();
    }
  }, [accountNumber]);

  const loadAccount = async () => {
    try {
      const data = await apiService.getAccount(accountNumber!);
      setAccount(data);
    } catch (error) {
      console.error('Error loading account:', error);
    }
  };

  const loadTransactions = async () => {
    try {
      const data = await apiService.getAccountTransactions(accountNumber!);
      setTransactions(data);
    } catch (error) {
      console.error('Error loading transactions:', error);
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

  if (!account) {
    return (
      <Layout>
        <div className="text-center py-12">
          <p className="text-gray-500">Account not found</p>
          <Link to="/accounts" className="text-primary-600 hover:text-primary-500 mt-4 inline-block">
            Back to Accounts
          </Link>
        </div>
      </Layout>
    );
  }

  return (
    <Layout>
      <div className="px-4 sm:px-0">
        <Link
          to="/accounts"
          className="inline-flex items-center text-sm text-gray-500 hover:text-gray-700 mb-4"
        >
          <ArrowLeft className="h-4 w-4 mr-2" />
          Back to Accounts
        </Link>

        {/* Account Info Card */}
        <div className="bg-white shadow rounded-lg p-6 mb-6">
          <div className="flex justify-between items-start mb-4">
            <div>
              <h1 className="text-2xl font-bold text-gray-900">{account.accountType} Account</h1>
              <p className="text-sm text-gray-500 mt-1">Account Number: {account.accountNumber}</p>
            </div>
            <span className={`inline-flex items-center px-3 py-1 rounded-full text-sm font-medium ${
              account.isActive
                ? 'bg-green-100 text-green-800' 
                : 'bg-gray-100 text-gray-800'
            }`}>
              {account.isActive ? 'ACTIVE' : 'INACTIVE'}
            </span>
          </div>

          <div className="border-t border-gray-200 pt-4">
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div>
                <p className="text-sm text-gray-500">Balance</p>
                <p className="text-2xl font-bold text-gray-900 mt-1">
                  ₹{parseFloat(account.balance.toString()).toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                </p>
              </div>
              {account.minimumBalance && (
                <div>
                  <p className="text-sm text-gray-500">Minimum Balance</p>
                  <p className="text-lg font-semibold text-gray-900 mt-1">
                    ₹{parseFloat(account.minimumBalance.toString()).toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                  </p>
                </div>
              )}
              {account.interestRate && (
                <div>
                  <p className="text-sm text-gray-500">Interest Rate</p>
                  <p className="text-lg font-semibold text-gray-900 mt-1">
                    {(account.interestRate * 100).toFixed(2)}%
                  </p>
                </div>
              )}
            </div>
          </div>
        </div>

        {/* Quick Actions */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
          <Link
            to={`/transactions/deposit?account=${account.accountNumber}`}
            className="bg-white shadow rounded-lg p-4 hover:shadow-md transition flex items-center"
          >
            <ArrowDownCircle className="h-8 w-8 text-green-600 mr-4" />
            <div>
              <p className="font-medium text-gray-900">Deposit</p>
              <p className="text-sm text-gray-500">Add funds to account</p>
            </div>
          </Link>
          <Link
            to={`/transactions/withdraw?account=${account.accountNumber}`}
            className="bg-white shadow rounded-lg p-4 hover:shadow-md transition flex items-center"
          >
            <ArrowUpCircle className="h-8 w-8 text-red-600 mr-4" />
            <div>
              <p className="font-medium text-gray-900">Withdraw</p>
              <p className="text-sm text-gray-500">Withdraw funds</p>
            </div>
          </Link>
          <Link
            to={`/transactions/transfer?account=${account.accountNumber}`}
            className="bg-white shadow rounded-lg p-4 hover:shadow-md transition flex items-center"
          >
            <ArrowLeftRight className="h-8 w-8 text-blue-600 mr-4" />
            <div>
              <p className="font-medium text-gray-900">Transfer</p>
              <p className="text-sm text-gray-500">Transfer to another account</p>
            </div>
          </Link>
        </div>

        {/* Transactions */}
        <div className="bg-white shadow rounded-lg">
          <div className="px-6 py-4 border-b border-gray-200">
            <h2 className="text-lg font-medium text-gray-900">Transaction History</h2>
          </div>
          {transactions.length === 0 ? (
            <div className="text-center py-12">
              <p className="text-gray-500">No transactions found</p>
            </div>
          ) : (
            <div className="divide-y divide-gray-200">
              {transactions.map((transaction) => (
                <div key={transaction.id} className="px-6 py-4">
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
                      <p className="text-xs text-gray-500">
                        Balance: ₹{parseFloat(transaction.balanceAfterTransaction.toString()).toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
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
              ))}
            </div>
          )}
        </div>
      </div>
    </Layout>
  );
};

