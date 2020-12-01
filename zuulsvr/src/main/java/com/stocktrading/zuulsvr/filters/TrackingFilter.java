package com.stocktrading.zuulsvr.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.stocktrading.zuulsvr.entity.User;
import com.stocktrading.zuulsvr.exception.ApiResponse;
import com.stocktrading.zuulsvr.repository.GenericDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class TrackingFilter extends ZuulFilter
{
    private static final int FILTER_ORDER = 10;
    private static final boolean SHOULD_FILTER = true;
    private static final Logger logger = LoggerFactory.getLogger(TrackingFilter.class);
    
    @Qualifier("userDataRepository")
    @Autowired
    GenericDataRepository userDataRepository;
    
    @Autowired
    FilterUtils filterUtils;
    
    @Override
    public String filterType()
    {
        return FilterUtils.PRE_FILTER_TYPE;
    }
    
    @Override
    public int filterOrder()
    {
        return FILTER_ORDER;
    }
    
    public boolean shouldFilter()
    {
        return SHOULD_FILTER;
    }
    
    private boolean isCorrelationIdPresent()
    {
        if (filterUtils.getCorrelationId() != null)
        {
            return true;
        }
        
        return false;
    }
    
    private String generateCorrelationId()
    {
        return java.util.UUID.randomUUID().toString();
    }
    
    @Override
    public Object run()
    {
        RequestContext ctx = RequestContext.getCurrentContext();
        
        //ctx.addZuulRequestHeader("Cookie", "SESSION=");
        logger.debug("Processing incoming request for {}.", ctx.getRequest().getRequestURI());
        
        System.out.println("*** Checking token ***");
        User user = isTokenPresentAndValid();
        if (user == null)
        {
            return new ApiResponse(HttpStatus.FORBIDDEN, "Missing or Unauthorised token", HttpStatus.FORBIDDEN.getReasonPhrase());
        }
        else
        {
            filterUtils.setAuthToken(user.getToken());
        }
        
        System.out.println("*************** Processing tracking pre filter ***************");
        if (isCorrelationIdPresent())
        {
            logger.debug("tmx-correlation-id found in tracking filter: {}. ", filterUtils.getCorrelationId());
        }
        else
        {
            filterUtils.setCorrelationId(generateCorrelationId());
            logger.debug("tmx-correlation-id generated in tracking filter: {}.", filterUtils.getCorrelationId());
        }
        
        return null;
    }
    
    
    public User isTokenPresentAndValid()
    {
        if (filterUtils.getAuthToken() == null)
        {
            return null;
        }
        else
        {
            String authToken = filterUtils.getAuthToken();
            User user = (User) userDataRepository.getRecordByEntityProperty("token", authToken);
            if (user != null)
            {
                return user;
            }
            else
            {
                return null;
            }
        }
    }
}