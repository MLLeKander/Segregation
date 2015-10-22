function plotMetrics2(data, postfix)
    function [h1, h2] = plotHist(func1, func2, heading, fileBase, figureNum)
        if ~ishandle(figureNum)
            figure(figureNum)
        end
        set(0, 'CurrentFigure', figureNum);
        clf;
        tmp1 = func1;
        tmp2 = func2;
        h1 = histogram(tmp1);
        hold on;
        h2 = histogram(tmp2);
        newWidth = mean([h1.BinWidth h2.BinWidth]);
        h1.BinWidth = newWidth;
        h2.BinWidth = newWidth;

        %dataMin = min([tmp1 tmp2]);
        %dataMax = max([tmp1 tmp2]);
        %edges = linspace(dataMin,dataMax,100);
        %cnts1 = histc(tmp1, edges);
        %cnts2 = histc(tmp2, edges);
        %bar(edges,cnts1,'FaceColor', 'r');
        %hold on;
        %bar(edges,cnts2,'FaceColor', 'b');

        xlabel(heading);
        ylabel('Counts');
        title(sprintf('%s\n(features=%d, emptiness=%.0d%%, %dx%d, %s, %d runs)',heading,numFeatures,emptiness*100,cols,rows,strat, length(tmp1)));
        legend('Initial','Final');
        drawnow;
        print(sprintf('%s%s.png',fileBase,postfix),'-dpng');

        if ~ishandle(figureNum+10)
            figure(figureNum+10)
        end
        set(0, 'CurrentFigure', figureNum+10);
        clf
        histogram(tmp2-tmp1);
        title(sprintf('%s (diff)\n(features=%d, emptiness=%.0d%%, %dx%d, %s, %d runs)',heading,numFeatures,emptiness*100,cols,rows,strat, length(tmp1)));
        xlabel('After - Before');
        ylabel('Counts');
        drawnow;
        print(sprintf('%s%s_diff.png',fileBase,postfix),'-dpng');
    end

    numFeatures = data.numFeatures(1);
    emptiness = data.emptiness(1);
    cols = data.cols(1);
    rows = data.rows(1);
    strat = data.strat(1);

    if nargin < 2
        postfix = sprintf('_%02d_%.0d_%dx%d_artificial',numFeatures, emptiness*100, cols, rows, strat(1));
    end
    
    identityFunc = @(x)(x);
    [h1,h2] = plotHist(data.beforeSimilarity, data.afterSimilarity, 'Average Agent Similarity Score', 'Similarity', 1);
    [h1,h2] = plotHist(data.beforeMooreClustering, data.afterMooreClustering, 'Number of Moore Clusters', 'Moore', 2);
    [h1,h2] = plotHist(data.beforeSingleFeatureMooreClustering, data.afterSingleFeatureMooreClustering, 'Number of Moore Clusters (first feature)', 'Moore1', 3);
    [h1,h2] = plotHist(data.beforeNeumannClustering, data.afterNeumannClustering, 'Number of von Neumann Clusters', 'Neumann', 4);
    [h1,h2] = plotHist(data.beforeSingleFeatureNeumannClustering, data.afterSingleFeatureNeumannClustering, 'Number of von Neumann Clusters (first feature)', 'Neumann1', 5);
end
