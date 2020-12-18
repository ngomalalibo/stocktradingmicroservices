package com.stocktrading.zuulsvr.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.stocktrading.zuulsvr.exception.ApiResponse;
import com.stocktrading.zuulsvr.repository.GenericDataRepository;
import com.stocktrading.zuulsvr.security.ServiceConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class TrackingFilter extends ZuulFilter
{
    private static final int FILTER_ORDER = 1;
    private static final boolean SHOULD_FILTER = true;
    private static final Logger logger = LoggerFactory.getLogger(TrackingFilter.class);
    
    @Qualifier("userDataRepository")
    @Autowired
    GenericDataRepository userDataRepository;
    
    @Autowired
    FilterUtils filterUtils;
    
    @Autowired
    ServiceConfig serviceConfig;
    
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
        
        String token = isTokenPresentAndValid();
        if (token == null)
        {
            return new ApiResponse(HttpStatus.FORBIDDEN, "Missing or Unauthorised token", HttpStatus.FORBIDDEN.getReasonPhrase());
        }
        else
        {
            filterUtils.setAuthToken(token);
            if (isCorrelationIdPresent())
            {
                logger.info("tmx-correlation-id found in tracking filter: {}. ", filterUtils.getCorrelationId());
                logger.info("Client ID found in tracking filter: {}. ", getClientId());
            }
            else
            {
                filterUtils.setCorrelationId(generateCorrelationId());
                logger.info("tmx-correlation-id generated in tracking filter: {}.", filterUtils.getCorrelationId());
                logger.info("Client ID found in tracking filter: {}. ", getClientId());
            }
            
        }
        
        
        return null;
    }
    
    
    public String isTokenPresentAndValid()
    {
        
        if (filterUtils.getAuthToken() == null)
        {
            return null;
        }
        else
        {
            return filterUtils.getAuthToken();
        }
    }
    
    private String getClientId()
    {
        String result = "";
        if (filterUtils.getAuthToken() != null)
        {
            String authToken = filterUtils
                    .getAuthToken()
                    .replace("Bearer ", "");
            try
            {
                Claims claims =
                        Jwts.parser().setSigningKey(serviceConfig.getJwtSigningKey().getBytes("UTF-8"))
                            .parseClaimsJws(authToken)
                            .getBody();
                result = (String) claims.get("clientId");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return result;
    }
}