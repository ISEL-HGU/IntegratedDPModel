import pandas as pd
import numpy as np
from patsy import dmatrices
import statsmodels.api as sm
from statsmodels.stats.outliers_influence import variance_inflation_factor

housing = pd.read_csv("bugzilla.csv")

# see the basic info
housing.info()
features = "transactionid+commitdate+ns+nm+nf+entropy+la+ld+lt+fix+ndev+pd+npt+exp+rexp+sexp+bug"
y, X = dmatrices("bug ~" +features, data=housing, return_type="dataframe")
vif = [variance_inflation_factor(X.values, i) for i in range(X.shape[1])]
result = sm.OLS(y, X).fit()
print(result.summary())
